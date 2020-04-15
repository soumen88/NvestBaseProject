package nvest.com.nvestlibrary.commonMethod;

import androidx.core.content.FileProvider;

/**
 * We need this class for android.support.v4.content.FileProvider
 * Since Android Nougat we need this to be declared in AndroidManifest.xml
 * in order to open PDF file in another application
 */
public class GenericFileProvider extends FileProvider {
}
