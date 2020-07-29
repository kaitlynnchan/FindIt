package cmpt276.project.flickr;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, PhotoGalleryActivity.class);
    }
}
