package nvest.com.nvestlibrary.summary;

import android.Manifest;
import android.app.Activity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PdfGenerator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import nvest.com.nvestlibrary.R;
import nvest.com.nvestlibrary.commonMethod.CommonMethod;
import nvest.com.nvestlibrary.commonMethod.GenericDTO;
import nvest.com.nvestlibrary.commonMethod.NvestLibraryConfig;
import nvest.com.nvestlibrary.generatePdf.PdfUtils;
import nvest.com.nvestlibrary.nvestCursorModel.Products;
import nvest.com.nvestlibrary.nvestWebModel.ValidationIP;
import nvest.com.nvestlibrary.validateinformation.ValidateInformationDataViewModel;

public class SummaryFragment extends Fragment implements PdfGenerator.OnPdfGeneratedListener, ValidateInformationDataViewModel.ValidateInformationDataListener, SummaryVieModel.SummaryListener {
    private static final String TAG = SummaryFragment.class.getSimpleName();
    private Context context;
    private Activity activity;

    private Products product;
    private PdfUtils pdfUtils;
    private ValidateInformationDataViewModel validateInformationDataViewModel;
    private SummaryVieModel summaryVieModel;
    private List<Object> summaryList;
    private SummaryAdapter summaryAdapter;
    private long startTime, endTime, duration;
    private String biHtml;

    // views
    private TextView tvProductName, tvSummaryTotal;
    private Button btnDownloadPdf;
    private ProgressBar progressBar;
    private RecyclerView rvSummary;
    private ValidationIP validationIp;
    private List<ValidationIP> validationIpList;
    private AlertDialog storagePermissionExplainerDialog, permissionDeniedDialog;

    // asynctask objects
    private GenerateBiQuotationTask generateBiQuotationTask;
    private GenerateBiTask generateBiTask;
    private GenerateHtmlTask generateHtmlTask;
    private GeneratePdfTask generatePdfTask;
    private static final String GENERATION_BI_QUOTATION_TASK = GenerateBiQuotationTask.class.getSimpleName();
    private static final String GENERATION_BI_TASK = GenerateBiTask.class.getSimpleName();
    private static final String GENERATION_HTML_TASK = GenerateHtmlTask.class.getSimpleName();
    private static final String GENERATION_PDF_TASK = GeneratePdfTask.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        context = getContext();
        activity = getActivity();


        generateBiQuotationTask = new GenerateBiQuotationTask();
        generateBiTask = new GenerateBiTask();
        generateHtmlTask = new GenerateHtmlTask();
        generatePdfTask = new GeneratePdfTask();

        Products productRecieved = getArguments().getParcelable("products");
        if (productRecieved != null) {
            product = productRecieved;
        }

        // register views
        tvProductName = view.findViewById(R.id.text_product_name);
        tvSummaryTotal = view.findViewById(R.id.tvSummaryTotal);
        btnDownloadPdf = view.findViewById(R.id.btnDownloadPdf);
        progressBar = view.findViewById(R.id.progressBar);
        rvSummary = view.findViewById(R.id.rvSummary);


        // set product name in title
        tvProductName.setText(product.getProductName());

        storagePermissionExplainerDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(R.string.permission_storage_explanation)
                .setPositiveButton(R.string.permission_storage_request_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestStoragePermission();
                    }
                }).setNegativeButton(R.string.permission_storage_not_now_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hideProgressBar();
                    }
                }).create();

        permissionDeniedDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_storage_permission_denied_dialog_title)
                .setMessage(R.string.permission_storage_permission_denied_message)
                .setPositiveButton(R.string.permission_storage_retry_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestStoragePermission();
                    }
                }).setNegativeButton(R.string.permission_storage_i_am_sure_dialog_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        hideProgressBar();
                    }
                }).create();

        // register listeners
        btnDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf();
            }
        });

        init();
        return view;
    }

    private void init() {

        summaryList = new ArrayList<>();
        summaryAdapter = new SummaryAdapter(context, summaryList);
        validateInformationDataViewModel = ViewModelProviders.of(this).get(ValidateInformationDataViewModel.class);
        validateInformationDataViewModel.setValidateInformationDataListener(this);

        summaryVieModel = ViewModelProviders.of(this).get(SummaryVieModel.class);
        summaryVieModel.setSummaryListener(this);

        rvSummary.setItemAnimator(new DefaultItemAnimator());
        rvSummary.setLayoutManager(new LinearLayoutManager(context));
        rvSummary.setHasFixedSize(true);
        rvSummary.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvSummary.setAdapter(summaryAdapter);
        rvSummary.setNestedScrollingEnabled(false);

        validationIp = GenericDTO.getAttributeValue(NvestLibraryConfig.VALIDATION_IP);
        validationIpList = GenericDTO.getAttributeValue(NvestLibraryConfig.LIST_VALIDATION_IP);

        generateSummaryList();

        generateBiTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // startFirstTask();

    }

    private void generateSummaryList() {

        float productAnnualPremium = validationIp.getAnnualPremium();
        float productModalPremium = validationIp.getModalPremium();
        float productTax = validationIp.getTax();
        float productModalPremiumWithTax = productModalPremium + productTax;
        float summaryTotal = productModalPremiumWithTax;

        SummaryEntry summaryEntry = new SummaryEntry(SummaryEntry.ANNUAL_PREMIUM, productAnnualPremium);
        summaryList.add(summaryEntry);

        summaryEntry = new SummaryEntry(SummaryEntry.INSTALLMENT_PREMIUM, productModalPremium);
        summaryList.add(summaryEntry);

        summaryEntry = new SummaryEntry(SummaryEntry.GST, productTax);
        summaryList.add(summaryEntry);

        summaryEntry = new SummaryEntry(SummaryEntry.INSTALLMENT_PREMIUM_WITH_GST, productModalPremiumWithTax);
        summaryList.add(summaryEntry);

        if (validationIpList != null) {
            for (ValidationIP riderValidationIP : validationIpList) {
                String riderName = riderValidationIP.getProductName();
                float riderAnnualPremium = riderValidationIP.getAnnualPremium();
                float riderModalPremium = riderValidationIP.getModalPremium();
                float riderTax = riderValidationIP.getTax();
                float riderModalPremiumWithTax = riderModalPremium + riderTax;

                summaryList.add(riderName);

                summaryEntry = new SummaryEntry(SummaryEntry.ANNUAL_PREMIUM, riderAnnualPremium);
                summaryList.add(summaryEntry);

                summaryEntry = new SummaryEntry(SummaryEntry.INSTALLMENT_PREMIUM, riderModalPremium);
                summaryList.add(summaryEntry);

                summaryEntry = new SummaryEntry(SummaryEntry.GST, riderTax);
                summaryList.add(summaryEntry);

                summaryEntry = new SummaryEntry(SummaryEntry.INSTALLMENT_PREMIUM_WITH_GST, riderModalPremiumWithTax);
                summaryList.add(summaryEntry);

                summaryTotal += riderModalPremiumWithTax;
            }
        }

        summaryAdapter.notifyDataSetChanged();


        tvSummaryTotal.setText(String.format("%s %s", getString(R.string.rs), CommonMethod.getCommaSeparatedAmount(summaryTotal, 2)));
    }


    private void downloadPdf() {
        //new GeneratePdfTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        generatePdfTask = new GeneratePdfTask();
        generatePdfTask.execute();
    }


    @Override
    public void onCompleteValidation(MutableLiveData<ValidationIP> validationIpLiveData) {

    }

    @Override
    public void onCompleteRidersValidation(MutableLiveData<List<ValidationIP>> validIpListLiveData) {

    }

    @Override
    public void onBiGenerated(LinkedHashMap<Integer, HashMap<String, String>> biData) {
        if (biData != null) {
            pdfUtils = new PdfUtils(getContext(), product, biData);
            pdfUtils.setValidationIp(validationIp);
            pdfUtils.setValidationIpList(validationIpList);
            pdfUtils.setOnPdfGeneratedListener(this);
            try {
                biHtml = generateHtmlTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBiUlipGenerated(LinkedHashMap<Integer, HashMap<String, String>>[] biData) {
        if (biData != null) {
            pdfUtils = new PdfUtils(getContext(), product, biData);
            pdfUtils.setValidationIp(validationIp);
            pdfUtils.setValidationIpList(validationIpList);
            pdfUtils.setOnPdfGeneratedListener(this);
            try {
                biHtml = generateHtmlTask.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void generatePdf(PdfUtils pdfUtil, String biHtml) {


        // save pdf in external documents directory
        File path;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Nvest/");
        } else {
            path = new File(Environment.getExternalStorageDirectory() + "/Documents/Nvest/");
        }

        try {
            String pdfFileName = pdfUtil.getPdfFileName(product);
            pdfUtil.generatePdf(path, biHtml, pdfFileName);
        } catch (Exception e) {
            e.printStackTrace();
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressBar();
                    enableButton(btnDownloadPdf);
                }
            });
        }
    }

    private void savePdf() {
        startTime = System.currentTimeMillis();

        disableButton(btnDownloadPdf);
        generatePdf(pdfUtils, biHtml);
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, NvestLibraryConfig.STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NvestLibraryConfig.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePdf();
            } else {
                permissionDeniedDialog.show();
            }
        }
    }

    private void showProgressBar() {
        if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() != View.GONE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void enableButton(Button btn) {
        if (!btn.isEnabled()) {
            btn.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn.setBackgroundColor(getResources().getColor(R.color.colorRed, null));
                btn.setTextColor(getResources().getColor(android.R.color.white, null));
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btn.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void disableButton(Button btn) {
        if (btn.isEnabled()) {
            btn.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn.setBackgroundColor(getResources().getColor(R.color.colorRedDisabled, null));
                btn.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.colorRedDisabled));
                btn.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    @Override
    public void pdfGenerated(String pdfFilePath, String pdfFileName) {
        try {
            Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(pdfFilePath));
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.setDataAndType(path, "application/pdf");
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open pdf file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open " + pdfFileName + ". Please try again later.", Toast.LENGTH_SHORT).show();
        } finally {
            endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            Toast.makeText(context, String.format("%s %s %s %s", pdfFileName, "generated in", duration, "ms\n\n"), Toast.LENGTH_SHORT).show();
            System.out.println(String.format("%s: %s %s %s", "DURATION:", "PDF Generated in", duration, "ms\n\n"));
            enableButton(btnDownloadPdf);
            hideProgressBar();
            generateBiQuotationTask = new GenerateBiQuotationTask();
            generateBiQuotationTask.execute();
        }
    }

    @Override
    public void biQuotationGenerated() {
        CommonMethod.log(TAG, "Bi quotation generated");
    }

    class GenerateHtmlTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonMethod.log(TAG, String.format("%s started", GENERATION_HTML_TASK));
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            CommonMethod.log(TAG, String.format("%s finished", GENERATION_HTML_TASK));
            btnDownloadPdf.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return pdfUtils.generateNormalProductHtml();
        }
    }

    class GenerateBiTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonMethod.log(TAG, String.format("%s started", GENERATION_BI_TASK));
            showProgressBar();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CommonMethod.log(TAG, String.format("%s finished", GENERATION_BI_TASK));
            hideProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, String> params = CommonMethod.getAllParamsFromGenericDTO();
            CommonMethod.log(TAG , "Params " + params.toString());
            if (product.isUlip()) {
                validateInformationDataViewModel.GenerateBIULIP(params);
            } else {
                validateInformationDataViewModel.GenerateBI(params);
            }
            return null;
        }
    }

    class GeneratePdfTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressBar();
            disableButton(btnDownloadPdf);
            CommonMethod.log(TAG, String.format("%s started", GENERATION_PDF_TASK));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CommonMethod.log(TAG, String.format("%s finished", GENERATION_PDF_TASK));
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        storagePermissionExplainerDialog.show();
                    } else {
                        requestStoragePermission();
                    }
                } else {
                    savePdf();
                }
            } else {
                savePdf();
            }
            return null;
        }
    }

    class GenerateBiQuotationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonMethod.log(TAG, String.format("%s started", GENERATION_BI_QUOTATION_TASK));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CommonMethod.log(TAG, String.format("%s finished", GENERATION_BI_QUOTATION_TASK));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            summaryVieModel.generateBiQuotation(product);
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (generateBiQuotationTask != null) {
            generateBiQuotationTask.cancel(true);
        }
        if (generateBiTask != null) {
            generateBiTask.cancel(true);
        }

        if (generateHtmlTask != null) {
            generateHtmlTask.cancel(true);
        }

        if (generatePdfTask != null) {
            generatePdfTask.cancel(true);
        }
    }

}
