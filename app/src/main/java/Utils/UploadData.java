package Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadData {
  private static String downloadUrl=null;

  public static void upload(ImageUploadCallBack callBack, String path, Context context) {
    if (path != null) {
      FirebaseStorage storage = FirebaseStorage.getInstance();
      final StorageReference photosRef = storage.getReference().child("photos/");
      InputStream stream = null;
      try {
        stream = new FileInputStream(new File(path));
      } catch (FileNotFoundException e) {
        Log.d("ADD", path);
        e.printStackTrace();
      }

      photosRef.putStream(stream).addOnSuccessListener(taskSnapshot -> photosRef.getDownloadUrl().addOnSuccessListener(uri -> {
        Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
        downloadUrl = uri.toString();
        callBack.onSuccess(downloadUrl);
      }));
    } else {
      callBack.onSuccess(downloadUrl);
    }

  }
}


