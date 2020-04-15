package nvest.com.nvestlibrary.generatePdf;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import android.database.Cursor;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nvest.com.nvestlibrary.generatePdf.models.OutputSummary;
import nvest.com.nvestlibrary.nvestDatabaseAccess.NvestAssetDatabaseAccess;

public class GeneratePdfDataViewModel extends AndroidViewModel {
    private static String TAG = GeneratePdfDataViewModel.class.getSimpleName();
    private GeneratePdfDataViewModelListener generatePdfDataViewModelListener;
    private NvestAssetDatabaseAccess nvestDb;

    public GeneratePdfDataViewModel(@NonNull Application application) {
        super(application);
        nvestDb = NvestAssetDatabaseAccess.getSingletonInstance();
    }

    public GeneratePdfDataViewModelListener getGeneratePdfDataViewModelListener() {
        return generatePdfDataViewModelListener;
    }

    public void setGeneratePdfDataViewModelListener(GeneratePdfDataViewModelListener generatePdfDataViewModelListener) {
        this.generatePdfDataViewModelListener = generatePdfDataViewModelListener;
    }

    public interface GeneratePdfDataViewModelListener {
    }

    public int getSectionCount(int productId) {
        String query = "select Count(distinct Section) from OutputSummary where ProductId = " + productId;
        Cursor cursor = nvestDb.ExecuteQuery(query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }

    public List<OutputSummary> getOutputSummary(String productId) {
        List<OutputSummary> outputSummaries = new ArrayList<>();
        String query = "select * from OutputSummary where ProductId = " + productId + " order by Section";
        Cursor cursor = nvestDb.ExecuteQuery(query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                OutputSummary outputSummary = new OutputSummary();
                outputSummary.setId(cursor.getInt(0));
                outputSummary.setProductId(cursor.getInt(1));
                outputSummary.setOutputKeywordName(cursor.getString(2));
                outputSummary.setOutputKeyword(cursor.getString(3));
                outputSummary.setBiRow(cursor.getString(4));
                outputSummary.setDisplay(cursor.getInt(5));
                outputSummary.setDisplayType(cursor.getString(6));
                outputSummary.setSection(cursor.getString(7));
                outputSummary.setOptionId(cursor.getString(8));
                outputSummaries.add(outputSummary);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return outputSummaries;
    }

    public HashMap<String, String> getProductOtherInfo(int productId) {
        HashMap<String, String> productOtherInfo = new HashMap<>();

        String query = "Select KeyField, ValueField from ProductOtherInfo where ProductId = " + productId;
        Cursor cursor = nvestDb.ExecuteQuery(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String keyField = cursor.getString(0);
                    String valueField = cursor.getString(1);
                    productOtherInfo.put(keyField, valueField);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }


        return productOtherInfo;
    }
}
