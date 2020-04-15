package android.print;

import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import androidx.annotation.RequiresApi;
import android.util.Log;



import java.io.File;

public class PdfGenerator {
    private static final String TAG = "PdfGenerator";
    private final PrintAttributes printAttributes;

    public PdfGenerator(PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void generate(final PrintDocumentAdapter printAdapter, final File path, final String fileName, final OnPdfGeneratedListener onPdfGeneratedListener) {
        printAdapter.onLayout(null, printAttributes, null, new PrintDocumentAdapter.LayoutResultCallback() {
            @Override
            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFile(path, fileName), new CancellationSignal(), new PrintDocumentAdapter.WriteResultCallback() {
                    @Override
                    public void onWriteFinished(PageRange[] pages) {
                        super.onWriteFinished(pages);
                        onPdfGeneratedListener.pdfGenerated(path.getPath() + "/" + fileName, fileName);
                    }

                });
            }
        }, null);
    }

    private ParcelFileDescriptor getOutputFile(File path, String fileName) {
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }

    public interface OnPdfGeneratedListener {
        void pdfGenerated(String pdfFilePath, String pdfFileName);
    }
}
