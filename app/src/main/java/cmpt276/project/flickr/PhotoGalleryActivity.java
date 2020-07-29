
package cmpt276.project.flickr;
/**
 * This activity calls the Photogallery fragment
 */
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

}
