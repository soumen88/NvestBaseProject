package nvest.com.nvestlibrary.generatePdf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.print.PdfGenerator;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.AssetsFileReader;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.ImageUtil;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.generatePdf.models.OutputSummary;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.DynamicParams;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.solutionDetails.PfSectionDetailsBi;
import nvest.com.nvestlibrary.solutionDetails.PfSectionMasterBi;
import nvest.com.nvestlibrary.solutionDetails.SolutionMaster;

public class PdfUtils {
    private static final String TAG = PdfUtils.class.getSimpleName();

    private Context context;
    private WebView pdfWebView;
    private StringBuilder htmlStringBuilder;
    private AssetsFileReader assetsFileReader;
    private String productId;
    private LinkedHashMap<Integer, HashMap<String, String>> nonUlipData;
    private LinkedHashMap<Integer, HashMap<String, String>>[] ulipData;
    private LinkedHashMap<Integer, HashMap<String, String>> comboData4, comboData8;
    private LinkedHashMap<Integer, HashMap<String, String>>[] comboData = new LinkedHashMap[2];
    private Cursor biMasterCursor, biDetailsCursor;
    private HashMap<String, String> firstHashMap, lastHashMap, currentHashMap;
    private GeneratePdfDataViewModel generatePdfDataViewModel;
    private List<OutputSummary> outputSummaries;
    private Products product;
    private List<Products> productList;
    private ValidationIP validationIp;
    private List<ValidationIP> validationIpList;
    private PdfGenerator.OnPdfGeneratedListener onPdfGeneratedListener;
    private float overallInstallmentPremium, overallGstFirstYear, overallGstSecondYear, overallTotalFirstYear, overallTotalSecondYear;
    private long startTime, endTime, duration;

    // constructor for combo products
    public PdfUtils(Context context, LinkedHashMap<Integer, HashMap<String, String>> comboData4, LinkedHashMap<Integer, HashMap<String, String>> comboData8, Cursor biMasterCursor, Cursor biDetailsCursor, List<Products> productList) {
        this.context = context;
        this.comboData4 = comboData4;
        this.comboData8 = comboData8;
        this.comboData[0] = this.comboData4;
        this.comboData[1] = this.comboData8;
        this.biMasterCursor = biMasterCursor;
        this.biDetailsCursor = biDetailsCursor;
        this.productList = productList;
    }

    // constructor for traditional product
    public PdfUtils(Context context, Products product, LinkedHashMap<Integer, HashMap<String, String>> nonUlipData) {
        this.context = context;
        this.product = product;
        this.productId = String.valueOf(product.getProductId());
        this.nonUlipData = nonUlipData;
        firstHashMap = nonUlipData.get(0);
        lastHashMap = nonUlipData.get(nonUlipData.size() - 1);
    }

    // constructor for ulip product
    public PdfUtils(Context context, Products product, LinkedHashMap<Integer, HashMap<String, String>>[] ulipData) {
        this.context = context;
        this.product = product;
        this.productId = String.valueOf(product.getProductId());
        this.ulipData = ulipData;
        firstHashMap = ulipData[0].get(0);
        lastHashMap = ulipData[0].get(ulipData[0].size() - 1);
    }

    public void setValidationIp(ValidationIP validationIp) {
        this.validationIp = validationIp;
    }

    public void setValidationIpList(List<ValidationIP> validationIpList) {
        this.validationIpList = validationIpList;
    }

    public void setOnPdfGeneratedListener(PdfGenerator.OnPdfGeneratedListener onPdfGeneratedListener) {
        this.onPdfGeneratedListener = onPdfGeneratedListener;
    }

    // generates html for the combo products
    public String generateComboProductsHtml() {

        // initialise hmtl string builder
        htmlStringBuilder = new StringBuilder();

        htmlStringBuilder.append("<!DOCTYPE html><html><body>");

        // add logo header in html
        // start main table
        String headerHtml = getTableHeader();
        htmlStringBuilder.append(headerHtml);

        // append summary table after output summary
        String summaryTable = getSummaryTable();
        htmlStringBuilder.append(summaryTable);

        // section's part
        List<PfSectionMasterBi> pfSectionMasterBiList = new ArrayList<>();
        if (biMasterCursor != null) {
            if (biMasterCursor.moveToFirst()) {
                do {
                    PfSectionMasterBi pfSectionMasterBi = new PfSectionMasterBi(biMasterCursor.getInt(0), biMasterCursor.getString(1), biMasterCursor.getString(2), biMasterCursor.getInt(3));
                    pfSectionMasterBiList.add(pfSectionMasterBi);
                } while (biMasterCursor.moveToNext());
            }
        }

        // get table keys and table row headers
        List<PfSectionDetailsBi> pfSectionDetailsBiList = new ArrayList<>();
        List<String> rowKeys = new ArrayList<>();
        // List<String> rowHeaders = new ArrayList<>();
        if (biDetailsCursor != null) {
            if (biDetailsCursor.moveToFirst()) {
                do {
                    int id = biDetailsCursor.getInt(0);
                    int productId = biDetailsCursor.getInt(1);
                    String outputKeyword = biDetailsCursor.getString(2);
                    String keywordHeader = biDetailsCursor.getString(3);
                    int isSum = biDetailsCursor.getInt(4);
                    int sectionId = biDetailsCursor.getInt(5);
                    int sequence = biDetailsCursor.getInt(6);

                    if (!rowKeys.contains(outputKeyword)) {
                        PfSectionDetailsBi pfSectionDetailsBi = new PfSectionDetailsBi(id, productId, outputKeyword, keywordHeader, isSum, sectionId, sequence);
                        pfSectionDetailsBiList.add(pfSectionDetailsBi);
                        rowKeys.add(outputKeyword);
                        // rowHeaders.add(keywordHeader);
                    }


                } while (biDetailsCursor.moveToNext());
            }
        }

        // start div before product table for page break
        htmlStringBuilder.append("<div style='page-break-inside:avoid; page-break-after:always;'>");


        // for (int i = 0; i < comboData.length; i++) { // main for loop starts here
        LinkedHashMap<Integer, HashMap<String, String>> biData = comboData8;

        // start product table
        htmlStringBuilder.append("<table style='border: 1px solid #AECFF7;table-layout:auto; border-collapse: collapse;width:100%'>");

        // start product table header
        htmlStringBuilder.append("<thead style=\"page-break-inside: avoid ;\">");


            /*List<HeaderRow> firstHeaderRow = new ArrayList<>();
            List<HeaderRow> secondHeaderRow = new ArrayList<>();


            for (PfSectionDetailsBi pfDetails : pfSectionDetailsBiList) {
                PfSectionMasterBi pfMasters = null;
                int sectionId = pfDetails.getSectionId();

                for (PfSectionMasterBi pfSectionMasterBi : pfSectionMasterBiList) {
                    if (pfSectionMasterBi.getDefaultSequence() == sectionId) {
                        pfMasters = pfSectionMasterBi;
                        break;
                    }
                }

                if (pfMasters != null) {
                    int rowspan = 1;
                    int colspan = 1;
                }
            }


            // start first header row
            htmlStringBuilder.append("<tr>");


            // build first header row
            for (HeaderRow headerRow : firstHeaderRow) {
                htmlStringBuilder.append("<th rowspan=\"");
                htmlStringBuilder.append(headerRow.getRowSpan());
                htmlStringBuilder.append("\" colspan=\"");
                htmlStringBuilder.append(headerRow.getColSpan());
                htmlStringBuilder.append("\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                htmlStringBuilder.append(headerRow.getValue());
                htmlStringBuilder.append("</th>");
            }

            // end first header row
            htmlStringBuilder.append("</tr>");

            // start second header row
            htmlStringBuilder.append("<tr>");


            // build second header row
            for (HeaderRow headerRow : secondHeaderRow) {
                htmlStringBuilder.append("<th rowspan=\"");
                htmlStringBuilder.append(headerRow.getRowSpan());
                htmlStringBuilder.append("\" colspan=\"");
                htmlStringBuilder.append(headerRow.getColSpan());
                htmlStringBuilder.append("\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                htmlStringBuilder.append(headerRow.getValue());
                htmlStringBuilder.append("</th>");
            }

            // end second header row
            htmlStringBuilder.append("</tr>");*/


            /*// start section header row
            htmlStringBuilder.append("<tr>");

            // build section headers
            for (PfSectionMasterBi pfSectionMasterBi : pfSectionMasterBiList) {
                String sectionHeader = pfSectionMasterBi.getSectionHeader().trim();
                if (!sectionHeader.isEmpty()) {
                    int colspan = 0;
                    for (PfSectionDetailsBi pfSectionDetailsBi : pfSectionDetailsBiList) {
                        if (pfSectionMasterBi.getDefaultSequence() == pfSectionDetailsBi.getSectionId()) {
                            colspan++;
                        }
                    }

                    htmlStringBuilder.append("<th colspan=\"");
                    htmlStringBuilder.append(colspan);
                    htmlStringBuilder.append("\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                    htmlStringBuilder.append(pfSectionMasterBi.getSectionHeader());
                    htmlStringBuilder.append("</th>");
                }
            }

            // end section header row
            htmlStringBuilder.append("</tr>");


            // start header row
            htmlStringBuilder.append("<tr>");

            // build header row
            int counter = 0;
            for (String rowHeader : rowHeaders) {
                int rowspan = 1;
                PfSectionDetailsBi pfSectionDetailsBi = pfSectionDetailsBiList.get(counter);
                for (PfSectionMasterBi pfSectionMasterBi : pfSectionMasterBiList) {
                    if (pfSectionMasterBi.getDefaultSequence() == pfSectionDetailsBi.getSectionId()) {
                        if (pfSectionMasterBi.getSectionHeader().trim().isEmpty()) {
                            rowspan++;
                        }
                    }
                }
                htmlStringBuilder.append("<th rowspan=\"");
                htmlStringBuilder.append(rowspan);
                htmlStringBuilder.append("\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                htmlStringBuilder.append(rowHeader);
                htmlStringBuilder.append("</th>");
                counter++;
            }

            // end header row
            htmlStringBuilder.append("</tr>");*/

        StringBuilder biHeader1 = new StringBuilder();
        StringBuilder biHeader2 = new StringBuilder();

        for (int j = 0; j < pfSectionDetailsBiList.size(); j++) {
            PfSectionDetailsBi pfDetails = pfSectionDetailsBiList.get(j);
            if (pfDetails.getSectionId() == 1) {
                biHeader1.append("<th rowspan=\"2\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                biHeader1.append(pfDetails.getKeywordHeader());
                biHeader1.append("</th>");
            } else {
                biHeader2.append("<th style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                biHeader2.append(pfDetails.getKeywordHeader());
                biHeader2.append("</th>");
            }
        }

        int colspan = 1;
        for (int j = 0; j < pfSectionMasterBiList.size(); j++) {
            PfSectionMasterBi pfMaster = pfSectionMasterBiList.get(j);
            if (!pfMaster.getSectionHeader().trim().isEmpty()) {
                biHeader1.append("<th colspan=\"");
                biHeader1.append(colspan);
                biHeader1.append("\" style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;background-color: #ebf7ff; color: #2860a3;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                biHeader1.append(pfMaster.getSectionHeader());
                biHeader1.append("</th>");
            } else {
                colspan++;
            }
        }

        // first header row
        htmlStringBuilder.append("<tr>");
        htmlStringBuilder.append(biHeader1.toString());
        htmlStringBuilder.append("</tr>");

        // second header row
        htmlStringBuilder.append("<tr>");
        htmlStringBuilder.append(biHeader2.toString());
        htmlStringBuilder.append("</tr>");


        // end product table header
        htmlStringBuilder.append("</thead>");

        // start product table body
        htmlStringBuilder.append("<tbody>");

        // add data in table
        for (Map.Entry<Integer, HashMap<String, String>> entry : biData.entrySet()) {
            htmlStringBuilder.append("<tr style='page-break-inside: avoid;'>");
            HashMap<String, String> rowValues = entry.getValue();
            TreeMap<String, String> rowValuesTreeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            rowValuesTreeMap.putAll(rowValues);
            for (String rowKey : rowKeys) {
                if (rowValues != null && rowKey != null && rowValues.containsKey(rowKey)) {
                    String value = CommonMethod.getCommaSeparatedAmount((Math.round(Double.parseDouble(rowValuesTreeMap.get(rowKey)))), 0);
                    htmlStringBuilder.append("<td style=\"font-family:Arial, sans-serif;font-size:9px;padding:10px 4px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;text-align:center;\" contenteditable=\"true\" class=\"edittd ui-droppable\">");
                    // htmlStringBuilder.append("<div style='text-align:center;'>");
                    htmlStringBuilder.append(value);
                    // htmlStringBuilder.append("</div>");
                    htmlStringBuilder.append("</td>");
                }
            }
            htmlStringBuilder.append("</tr>");
        }

        // end product table
        htmlStringBuilder.append("</tbody></table><br><br>");
        // } // main for loop ends here

        // end page break div
        htmlStringBuilder.append("</div>");

        // end main table
        htmlStringBuilder.append("</td></tr></tbody></table>");
        htmlStringBuilder.append("</body></html>");

        return htmlStringBuilder.toString();
    }

    // generates html for the pdf
    public String generateNormalProductHtml() {

        startTime = System.currentTimeMillis();

        // initialise variables
        htmlStringBuilder = new StringBuilder();

        htmlStringBuilder.append("<!DOCTYPE html><html><body>");


        // add logo header in html
        // start main table
        String headerHtml = getTableHeader();
        htmlStringBuilder.append(headerHtml);

        // add output summary on top
        generatePdfDataViewModel = ViewModelProviders.of((FragmentActivity) context).get(GeneratePdfDataViewModel.class);
        outputSummaries = generatePdfDataViewModel.getOutputSummary(productId);

        // append output summary table html
        String outputSummaryTable = getOutputSummaryTable(outputSummaries);
        if (outputSummaryTable != null) {
            htmlStringBuilder.append(outputSummaryTable);
        }

        // append summary table after output summary
        String summaryTable = getSummaryTable();
        htmlStringBuilder.append(summaryTable);

        // read product file html content
        assetsFileReader = new AssetsFileReader();
        String productFileName = getProductFileName(product);
        String fileContent = assetsFileReader.readFile(context.getAssets(), productFileName)
                .replace("</tbody>", "")
                .replace("</table>", "");

        // remove everything after table row ends
        String unnecessaryString = fileContent.substring(fileContent.lastIndexOf("</tr>")).replace("</tr>", "");
        fileContent = fileContent.replace(unnecessaryString, "");
        htmlStringBuilder.append("<div style='page-break-inside:avoid; page-break-after:always;'>");
        htmlStringBuilder.append(fileContent);

        // fetch template row from file content
        int indexOfTableBodyStart = htmlStringBuilder.lastIndexOf("<tr");
        int indexOfTableBodyEnd = htmlStringBuilder.lastIndexOf("</tr>");
        String templateRow = htmlStringBuilder
                .substring(indexOfTableBodyStart, indexOfTableBodyEnd) + "</tr>";


        if (isUlip()) {
            // this is ulip data
            Set<String> argumentSet = new HashSet<>();
            String temporaryRow = templateRow;
            temporaryRow = temporaryRow.replace("[", "@").replace("]", "");
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(temporaryRow);
            while (matcher.find()) {
                argumentSet.add(matcher.group().replace("@", ""));
            }


            for (int i = 0; i < ulipData.length; i++) {
                String grossYield = "";
                String netYield = "";
                String netYieldValueStr = ulipData[i].get(0).get("Net_Yield");
                if (i == 0) {
                    grossYield = "Gross Yield: 8%";
                } else if (i == 1) {
                    grossYield = "Gross Yield: 4%";
                }
                if (netYieldValueStr != null) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    float netYieldValue = Float.parseFloat(netYieldValueStr);
                    netYieldValue = netYieldValue * 100;
                    netYield = "Net Yield: " + df.format(netYieldValue) + "%";
                }

                // add gross yield field
                htmlStringBuilder.append("<div style='color:#2860a3'><span><p style='display:block;margin:8px 0px;font-weight:bold;font-size:10px;'>");
                htmlStringBuilder.append(grossYield);
                htmlStringBuilder.append("</p></span></div>");

                // add net yield field
                htmlStringBuilder.append("<div style='color:#2860a3'><span><p style='display:block;margin:8px 0px;font-weight:bold;font-size:10px;'>");
                htmlStringBuilder.append(netYield);
                htmlStringBuilder.append("</p></span></div>");

                if (i == 1) {
                    // htmlStringBuilder.append("<div style='page-break-inside:avoid; page-break-after:always;'>");
                    htmlStringBuilder.append(fileContent);
                }
                LinkedHashMap<Integer, HashMap<String, String>> dtref = ulipData[i];
                int rows = dtref.size();
                for (int j = 0; j < rows; j++) {
                    TreeMap<String, String> tempHashMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    tempHashMap.putAll(dtref.get(j));
                    String tempHTML = temporaryRow;
                    for (String arg : argumentSet) {
                        String tempval = tempHashMap.get(arg.toUpperCase());
                        if (tempval == null) {
                            tempval = "0";
                        }
                        String valueReceived = CommonMethod.getCommaSeparatedAmount((Math.round(Double.parseDouble(tempval))), 0).toString();
                        valueReceived = "<div style='text-align:center;'>" + valueReceived + "</div>";
                        tempHTML = tempHTML.replaceAll("@" + arg, valueReceived);
                    }
                    htmlStringBuilder.append(tempHTML);
                }

                // add data into table
                // each data acts as one row of table
/*                for (Map.Entry<Integer, HashMap<String, String>> entry : ulipData[i].entrySet()) {
                    HashMap<String, String> temp = entry.getValue();
                    String temporaryRow = templateRow;
                    for (Map.Entry<String, String> tempHashMap : temp.entrySet()) {
                        if (tempHashMap.getKey() != null && tempHashMap.getValue() != null) {
                            String key = "[" + tempHashMap.getKey().toLowerCase() + "]";
                            String value = "";
                            try {
                                value = "<div style='text-align:center;'>" + CommonMethod.getCommaSeparatedAmount((Math.round(Double.parseDouble(tempHashMap.getValue()))), 0) + "</div>";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String row = temporaryRow.toLowerCase();
                            //.replace("<span>", "<div style='text-align:center;'><span>")
                            //.replace("</span>", "</span></div>");
                            temporaryRow = row.replace(key, value);
                        }
                    }
                    htmlStringBuilder.append(temporaryRow);
                }*/

                // end product table
                htmlStringBuilder.append("</tbody></table>");
                htmlStringBuilder.append("</div>");

                // add break after first table
                if (i == 0) {
                    htmlStringBuilder.append("<br/>");
                }
            }
        } else if (isNonUlip()) {

            /*String nonUlipProductTable = getFieldReplacedRow(nonUlipData, templateRow, htmlStringBuilder);
            htmlStringBuilder.append(nonUlipProductTable);*/

            Set<String> argumentSet = new HashSet<>();
            String temporaryRow = templateRow;
            temporaryRow = temporaryRow.replace("[", "@").replace("]", "");
            Matcher matcher = Pattern.compile("@\\s*(\\w+)").matcher(temporaryRow);
            while (matcher.find()) {
                argumentSet.add(matcher.group().replace("@", ""));
            }


            LinkedHashMap<Integer, HashMap<String, String>> dtref = nonUlipData;
            int rows = dtref.size();
            for (int j = 0; j < rows; j++) {
                TreeMap<String, String> tempHashMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                tempHashMap.putAll(dtref.get(j));

                String tempHTML = temporaryRow;
                for (String arg : argumentSet) {
                    String tempval = tempHashMap.get(arg.toUpperCase());
                    if (tempval == null) {
                        tempval = "0";
                    }
                    String valueReceived = CommonMethod.getCommaSeparatedAmount((Math.round(Double.parseDouble(tempval))), 0).toString();
                    valueReceived = "<div style='text-align:center;'>" + valueReceived + "</div>";
                    tempHTML = tempHTML.replaceAll("@" + arg, valueReceived);
                }
                htmlStringBuilder.append(tempHTML);
            }

            // add data into table
            // each data acts as one row of table
            /*for (Map.Entry<Integer, HashMap<String, String>> entry : nonUlipData.entrySet()) {
                HashMap<String, String> temp = entry.getValue();
                String temporaryRow = templateRow;
                for (Map.Entry<String, String> tempHashMap : temp.entrySet()) {
                    if (tempHashMap.getKey() != null && tempHashMap.getValue() != null) {
                        String key = "[" + tempHashMap.getKey().toLowerCase() + "]";
                        String value = "";
                        try {
                            value = "<div style='text-align:center;'>" + CommonMethod.getCommaSeparatedAmount((Math.round(Double.parseDouble(tempHashMap.getValue()))), 0) + "</div>";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String row = temporaryRow.toLowerCase();
                        //.replace("<span>", "<div style='text-align:center;'><span>")
                        //.replace("</span>", "</span></div>");
                        temporaryRow = row.replace(key, value);
                    }
                }
                htmlStringBuilder.append(temporaryRow);
            }*/

            // end product table
            htmlStringBuilder.append("</tbody></table>");
            htmlStringBuilder.append("</div>");
        }


        // add disclaimer to html
        String disclaimerFileName = getProductDisclaimerFileName(product);
        String disclaimerFileContent = assetsFileReader.readFile(context.getAssets(), disclaimerFileName);
        htmlStringBuilder.append(disclaimerFileContent);

        // end main table
        htmlStringBuilder.append("</td></tr></tbody></table>");
        htmlStringBuilder.append("</body></html>");

        // replace template row with empty string which is present by default
        // and also un replaced values which might not have been replaced
        String htmlContent = htmlStringBuilder.toString()
                .replace(templateRow, "")
                .replaceAll("\\[[^\\[]*\\]", "");

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println(String.format("%s: %s %s %s", "DURATION:", "Html generated in", duration, "ms"));

        return htmlContent;
    }

    public void generatePdf(File path, String biHtml, String pdfFileName) {

        startTime = System.currentTimeMillis();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                pdfWebView = new WebView(context);

                // load html into webview
                // pdfWebView.clearCache(true);
                pdfWebView.loadData(biHtml, "text/html", "UTF-8");

                pdfWebView.setWebViewClient(new WebViewClient() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // job name for our application
                        String jobName = context.getString(R.string.app_name) + " Document";

                        // create landscape a4 size pdf
                        // a4 is supported but only in portrait mode
                        // that's why we have to create custom size
                        PrintAttributes.MediaSize customPdfSize = new PrintAttributes.MediaSize("NVEST_CUSTOM_PDF_SIZE", "NVEST_CUSTOM_PDF_SIZE", 11690, 8260);

                        // generate pdf attributes and properties
                        PrintAttributes attributes = new PrintAttributes.Builder()
                                .setMediaSize(customPdfSize)
                                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();

                        // generate print document adapter
                        PdfGenerator pdfGenerator = new PdfGenerator(attributes);
                        PrintDocumentAdapter printAdapter;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            printAdapter = pdfWebView.createPrintDocumentAdapter(jobName);
                        } else {
                            printAdapter = pdfWebView.createPrintDocumentAdapter();
                        }

                        // generate pdf
                        pdfGenerator.generate(printAdapter, path, pdfFileName, new PdfGenerator.OnPdfGeneratedListener() {
                            @Override
                            public void pdfGenerated(String pdfFilePath, String pdfFileName) {
                                endTime = System.currentTimeMillis();
                                duration = endTime - startTime;
                                System.out.println(String.format("%s: %s %s %s", "DURATION:", "Html converted to pdf in", duration, "ms"));
                                // let the caller know the pdf file path once pdf is generated
                                onPdfGeneratedListener.pdfGenerated(pdfFilePath, pdfFileName);
                            }
                        });
                    }
                });
            }
        });


    }

    // returns html for summary table
    private String getSummaryTable() {
        StringBuilder summaryHtmlStringBuilder = new StringBuilder();

        // rider table start
        summaryHtmlStringBuilder.append("<table style='table-layout:auto; border-collapse: collapse;width:100%'>");

        // start table header
        summaryHtmlStringBuilder.append("<thead style=' background: #ebf7ff;'> <tr> ");

        // get product other info

        HashMap<String, String> productOtherInfo = new HashMap<>();
        if (product != null) {
            productOtherInfo = generatePdfDataViewModel.getProductOtherInfo(product.getProductId());
        }

        // header cell html template
        String headerCell = "<th style='font-family:Arial; font-size:10px; text-align:center;padding:10px 4px; color: #2860A3;border-style:solid;border-width:1px;border-color: #2860A3;'> %s </th> ";

        // insert table headers, if they are null take default values
        String labelName = productOtherInfo.get("SUDBiTableName");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelName == null ? "Name" : labelName));

        String labelSumAssured = productOtherInfo.get("SUDBiTableSA");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelSumAssured == null ? "Sum Assured" : labelSumAssured));

        String labelPtPpt = productOtherInfo.get("SUDBiTablePTPPT");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelPtPpt == null ? "PT/ PPT" : labelPtPpt));

        String labelMode = productOtherInfo.get("SUDBiTableMode");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelMode == null ? "Mode" : labelMode));

        String labelInstallmentPremium = productOtherInfo.get("SUDBiTableModalPrem");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelInstallmentPremium == null ? "Installment Premium" : labelInstallmentPremium));

        String labelTax = productOtherInfo.get("SUDBiTableTaxAmt");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelTax == null ? "GST" : labelTax));

        String labelTotalPremium = productOtherInfo.get("SUDBiTableTotalPrem");
        summaryHtmlStringBuilder.append(String.format(headerCell, labelTotalPremium == null ? "Total" : labelTotalPremium));


        // end table header
        summaryHtmlStringBuilder.append("</tr> </thead>");

        // start table body
        summaryHtmlStringBuilder.append("<tbody>");

        // add product info in table
        if (validationIp != null) {
            String productRow = addSummaryValue(validationIp);
            summaryHtmlStringBuilder.append(productRow);
        }

        // start inserting riders in the table
        if (validationIpList != null) {
            for (ValidationIP riderValidationIP : validationIpList) {
                String riderRow = addSummaryValue(riderValidationIP);
                summaryHtmlStringBuilder.append(riderRow);
            }
        }

        // end table body
        summaryHtmlStringBuilder.append("</tbody>");

        // start table footer
        summaryHtmlStringBuilder.append("<tfoot><tr><td  colspan ='4' style='font-family:Arial;font-size:10px;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>Total</td>");

        String finalInstallmentPremiumDisplay = CommonMethod.getCommaSeparatedAmount(overallInstallmentPremium, 0);
        String finalGstDisplay = "<span>Yr: " + CommonMethod.getCommaSeparatedAmount(overallGstFirstYear, 0) + "</span><br/><br/><span>Yr2: " + CommonMethod.getCommaSeparatedAmount(overallGstSecondYear, 0) + "</span>";
        String finalOverallTotalDisplay = "<span>Yr: " + CommonMethod.getCommaSeparatedAmount(overallTotalFirstYear, 0) + "</span><br/><br/><span>Yr2: " + CommonMethod.getCommaSeparatedAmount(overallTotalSecondYear, 0) + "</span>";

        // add total annual premium
        summaryHtmlStringBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryHtmlStringBuilder.append(finalInstallmentPremiumDisplay);
        summaryHtmlStringBuilder.append("</td>");

        // add total gst
        summaryHtmlStringBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryHtmlStringBuilder.append(finalGstDisplay);
        summaryHtmlStringBuilder.append("</td>");

        // add overall total
        summaryHtmlStringBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryHtmlStringBuilder.append(finalOverallTotalDisplay);
        summaryHtmlStringBuilder.append("</td>");

        // end table footer
        summaryHtmlStringBuilder.append("</tr></tfoot>");

        // end table
        summaryHtmlStringBuilder.append("</table><br/>");

        return summaryHtmlStringBuilder.toString();
    }

    private String addSummaryValue(ValidationIP validationInput) {

        StringBuilder summaryRowBuilder = new StringBuilder();

        // start table row for product entry
        summaryRowBuilder.append("<tr>");

        // get values from
        String nameDisplay = validationInput.getProductName();
        float sumAssured = validationInput.getSA();
        String ptPptDisplay = validationInput.getPT() + "/ " + validationInput.getPPT();
        float annualPremium = validationInput.getModalPremium();
        float gstFirstYear = validationInput.getTax();
        float gstSecondYear = validationInput.getTaxYr2();
        float totalFirstYear = annualPremium + gstFirstYear;
        float totalSecondYear = annualPremium + gstSecondYear;

        // calculate overall totals for the table
        overallInstallmentPremium += annualPremium;
        overallGstFirstYear += gstFirstYear;
        overallGstSecondYear += gstSecondYear;
        overallTotalFirstYear += totalFirstYear;
        overallTotalSecondYear += totalSecondYear;

        // format display value
        String sumAssuredDisplay = CommonMethod.getCommaSeparatedAmount(sumAssured, 0);
        String annualPremiumDisplay = CommonMethod.getCommaSeparatedAmount(annualPremium, 0);
        String gstDisplay = "<span>Yr: " + CommonMethod.getCommaSeparatedAmount(gstFirstYear, 0) + "</span><br/><br/><span>Yr2: " + CommonMethod.getCommaSeparatedAmount(gstSecondYear, 0) + "</span>";
        String totalDisplay = "<span>Yr: " + CommonMethod.getCommaSeparatedAmount(totalFirstYear, 0) + "</span><br/><br/><span>Yr2: " + CommonMethod.getCommaSeparatedAmount(totalSecondYear, 0) + "</span>";
        DynamicParams modeDynamicParams = GenericDTO.getDynamicParamByKey(NvestLibraryConfig.INPUT_MODE_ANNOTATION);
        String modeDisplay = "";
        if (modeDynamicParams != null) {
            modeDisplay = modeDynamicParams.getFieldValue();
        }

        // add product name
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(nameDisplay);
        summaryRowBuilder.append("</td>");

        // add sum assured
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(sumAssuredDisplay);
        summaryRowBuilder.append("</td>");

        // add pt/ppt
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(ptPptDisplay);
        summaryRowBuilder.append("</td>");

        // add mode
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(modeDisplay);
        summaryRowBuilder.append("</td>");

        // add installment premium
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(annualPremiumDisplay);
        summaryRowBuilder.append("</td>");

        // add gst
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(gstDisplay);
        summaryRowBuilder.append("</td>");

        // add total
        summaryRowBuilder.append("<td style='font-family:Arial;font-size:10px;text-align:center;padding:5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal'>");
        summaryRowBuilder.append(totalDisplay);
        summaryRowBuilder.append("</td>");

        // end table row for product entry
        summaryRowBuilder.append("</tr>");

        return summaryRowBuilder.toString();
    }

    // returns html for top output summary table
    private String getOutputSummaryTable(List<OutputSummary> outputSummaries) {

        // starting of output summary table
        StringBuilder summaryStringBuilder = new StringBuilder("<table style='border: 1px solid #94BAE8;background-color: #EBF4FF; border-collapse: collapse;width:100%'><tbody><tr>");

        // total number of sections in table
        // that many tds or columns will be there in table
        int sectionCount = generatePdfDataViewModel.getSectionCount(Integer.parseInt(productId));


        // decide if we should add fund choices in pdf
        boolean shouldAddFundChoices = isUlip() && GenericDTO.dynamicParamsContainsKey(NvestLibraryConfig.FUND_ID_ANNOTATION);

        // add one more section for fund choices in ulip products
        if (shouldAddFundChoices) {
            sectionCount++;
        }

        // if there are no sections return null
        if (sectionCount < 1) {
            return null;
        }

        // loop through sections
        for (int currentSection = 1; currentSection <= sectionCount; currentSection++) {
            if (currentSection == 1) {
                summaryStringBuilder.append("<td style='vertical-align: top; border: none; font-size: 16px; letter-spacing: 1px; font-weight: 100; line-height: 20px;'><div style='padding: 10px;'><h4 style='font-family: Tahoma, Geneva, sans-serif; text-transform: uppercase'>Life Assured Details</h4>");
            } else if (currentSection == 2) {
                summaryStringBuilder.append("<td style='vertical-align: top; border: none; font-size: 16px; letter-spacing: 1px; font-weight: 100; line-height: 20px;'><div style='padding: 10px;'><h4 style='font-family: Tahoma, Geneva, sans-serif; text-transform: uppercase'>Other Details</h4>");
            } else if (currentSection == sectionCount && shouldAddFundChoices) {
                summaryStringBuilder.append("<td style='vertical-align: top; border: none; font-size: 16px; letter-spacing: 1px; font-weight: 100; line-height: 20px;'><div style='padding: 10px;'><h4 style='font-family: Tahoma, Geneva, sans-serif; text-transform: uppercase'>Fund Choices</h4>");
            } else {
                summaryStringBuilder.append("<td style='vertical-align: top; border: none; font-size: 16px; letter-spacing: 1px; font-weight: 100; line-height: 20px;'><div style='padding-top: 70px;padding-bottom: 10px;padding-left: 10px;padding-right: 10px'>");
            }


            // loop through output summary data
            for (OutputSummary summary : outputSummaries) {

                if (String.valueOf(currentSection).equals(summary.getSection())) {
                    String displayLabel = summary.getOutputKeywordName();
                    String key = summary.getOutputKeyword();
                    String displayType = summary.getDisplayType();
                    int display = summary.getDisplay();

                    String displayValue = null;


                    // if key has square brackets get it's value from hashmap provided by BI data
                    if (key.contains("[")) {
                        // check which hashmap to use, first or last
                        String biRow = summary.getBiRow();
                        if (biRow.toLowerCase().equalsIgnoreCase("first")) {
                            currentHashMap = firstHashMap;
                        } else if (biRow.toLowerCase().equalsIgnoreCase("last")) {
                            currentHashMap = lastHashMap;
                        }

                        // get the display value
                        String keyWithoutBrackets = key.replace("[", "").replace("]", "");
                        displayValue = currentHashMap.get(keyWithoutBrackets);
                    } else {
                        DynamicParams dynamicParam = GenericDTO.getDynamicParamByKey(key);
                        if (dynamicParam != null) {
                            displayValue = dynamicParam.getFieldValue();
                        }
                    }

                    if (displayValue != null) {
                        if (displayValue.equalsIgnoreCase("-999")) {
                            displayValue = "NA";
                        } else if (displayValue.equalsIgnoreCase("-998")) {
                            continue;
                        } else if (displayType != null) {
                            if (displayType.equalsIgnoreCase("number")) {
                                double value = Double.parseDouble(displayValue);
                                displayValue = CommonMethod.getCommaSeparatedAmount(value, 0);
                            } else if (displayType.equalsIgnoreCase("percent")) {
                                double value = Double.parseDouble(displayValue);
                                displayValue = CommonMethod.getCommaSeparatedAmount(value * 100, 2);
                            } else if (displayType.equalsIgnoreCase("checkbox")) {
                                displayValue = displayValue.equals("1") ? "Yes" : "No";
                            } else if (display == 1 && displayType.equalsIgnoreCase("date")) {
                                try {
                                    displayValue = calculateAge(displayValue);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    // if display value was not found neither in generic dto (params)
                    // nor in hashmap, display empty value

                    if (displayValue == null) {
                        displayValue = "";
                    }

                    // generate each summary value html
                    summaryStringBuilder.append("<div><span style='font-family: Tahoma, Geneva, sans-serif; font-size: 14px; letter-spacing: 1px;'>");
                    summaryStringBuilder.append(displayLabel);
                    summaryStringBuilder.append(":</span> <span style='font-family: Tahoma, Geneva, sans-serif;  font-size: 14px;font-weight: 600; letter-spacing: 1px;'>");
                    summaryStringBuilder.append(displayValue);
                    summaryStringBuilder.append("</span></div><br>");
                }
            }

            // if it's last section and if we should add fund choices
            // then add fund choices
            if (currentSection == sectionCount && shouldAddFundChoices) {
                List<String> fundKeys = GenericDTO.getKeysBySubString(NvestLibraryConfig.FUND_ID_ANNOTATION);
                for (String key : fundKeys) {
                    DynamicParams fundDetails = GenericDTO.getDynamicParamByKey(key);

                    if (fundDetails != null) {
                        String displayLabel = fundDetails.getFieldValue();
                        String displayValue = fundDetails.getFieldKey() + "%";

                        // generate each summary value html
                        summaryStringBuilder.append("<div><span style='font-family: Tahoma, Geneva, sans-serif; font-size: 14px; letter-spacing: 1px;'>");
                        summaryStringBuilder.append(displayLabel);
                        summaryStringBuilder.append(":</span> <span style='font-family: Tahoma, Geneva, sans-serif;  font-size: 14px;font-weight: 600; letter-spacing: 1px;'>");
                        summaryStringBuilder.append(displayValue);
                        summaryStringBuilder.append("</span></div><br>");
                    }
                }
            }

            // end current td
            summaryStringBuilder.append("</div></td>");
        }

        // end output summary table
        summaryStringBuilder.append("</tr></tbody></table><br/>");

        Log.d(TAG, "getOutputSummaryTable: " + currentHashMap);
        return summaryStringBuilder.toString();
    }

    // returns pdf file name
    public String getPdfFileName(Products product) {
        return String.format(NvestLibraryConfig.PDF_FILE_NAME, product.getProductName().replaceAll(" ", "_"));
    }

    // returns pdf file name
    public String getPdfFileName(SolutionMaster solution) {
        return String.format(NvestLibraryConfig.PDF_FILE_NAME, solution.getSolutionName().replaceAll(" ", "_"));
    }

    // returns main table header which contains logo etc.
    // this is repeated on every pdf page
    private String getTableHeader() {
        String logo = "data:image/png;base64," + ImageUtil.convert(ImageUtil.getBitmapFromAsset(context, "logo.png"));
        return "<table style='width:100%;'><thead><tr><th style='text-align:left; padding:12px;'><img src='" + logo + "' alt='logo' width='100' height='47'></th></tr></thead><tbody class='main-body'><tr><td>";
    }

    // returns product html file name
    private String getProductFileName(Products product) {
        return String.format(NvestLibraryConfig.PRODUCT_FILE_NAME, product.getProductId());
    }

    // returns product disclaimer html file name
    private String getProductDisclaimerFileName(Products product) {
        return String.format(NvestLibraryConfig.PRODUCT_DISCLAIMER_FILE_NAME, product.getProductId());
    }

    private boolean isUlip() {
        return product.isUlip();
    }

    private boolean isNonUlip() {
        return !product.isUlip();
    }

    @SuppressLint("SimpleDateFormat")
    private String calculateAge(String strDate) throws ParseException {

        Date date = new SimpleDateFormat("dd MM yyyy").parse(strDate);
        Date today = new Date();
        Calendar dateCalendar = Calendar.getInstance();
        Calendar currentCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        currentCalendar.setTime(today);

        int year = 0;
        int month = currentCalendar.get(Calendar.YEAR) > dateCalendar.get(Calendar.YEAR) ? 12 : 0;

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        int dateYear = dateCalendar.get(Calendar.YEAR);
        int dateMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int dateDay = dateCalendar.get(Calendar.DAY_OF_MONTH);

        System.out.println(String.format("%s: %s-%s-%s\t%s", "Date", dateYear, dateMonth, dateDay, date));
        System.out
                .println(String.format("%s: %s-%s-%s\t%s", "Today", currentYear, currentMonth, currentDay, today));

        if (currentMonth < dateMonth) {
            year = currentYear - dateYear - 1;
        } else if (currentMonth > dateMonth) {
            year = currentYear - dateYear;
        } else if (currentDay >= dateDay) {
            year = currentYear - dateYear;
        } else {
            year = currentYear - dateYear - 1;
        }

        if (year <= 0) {
            if (currentDay < dateDay) {
                month = month + currentMonth - dateMonth - 1;
            } else {
                month = month + currentMonth - dateMonth;
            }
            return month + (month > 1 ? " months" : " month");
        } else {
            return year + (year > 1 ? " years" : " year");
        }
    }
}