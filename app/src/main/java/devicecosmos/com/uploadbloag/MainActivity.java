package devicecosmos.com.uploadbloag;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    EditText eTitle,eDate,eTags,eDetails,etTime,etYoutube,etBlogID;
    ImageView eImageView;
    Button eUpload;
    private Uri mimageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private StorageReference mstorageRef;
    private DatabaseReference mDatabaseRef;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTitle=(EditText)findViewById(R.id.editTitle);
        eDate=(EditText)findViewById(R.id.editDate);
        eTags=(EditText)findViewById(R.id.editTag);
        eDetails=(EditText)findViewById(R.id.editDetails);
        etTime=(EditText)findViewById(R.id.editTime);
        etYoutube=(EditText)findViewById(R.id.editYoutube);
        etBlogID=(EditText)findViewById(R.id.editBlogID);
        eImageView=(ImageView)findViewById(R.id.editImage);
        eUpload=(Button)findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        findViewById(R.id.rel).requestFocus();


        mstorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        eUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadClicked();
            }
        });
    }

    public void ImageViewClicked(View view) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    private void UploadClicked() {

        if (eTitle.getText().toString().isEmpty())
        {
            eTitle.setError("Enter Title");
            eTitle.requestFocus();
            return;
        }
        if (eDate.getText().toString().isEmpty())
        {
            eDate.setError("Enter Date");
            eDate.requestFocus();
            return;
        }
        if (etTime.getText().toString().isEmpty())
        {
            etTime.setError("Enter Time");
            etTime.requestFocus();
            return;
        }
        if (eTags.getText().toString().isEmpty())
        {
            eTags.setError("Enter Tags");
            eTags.requestFocus();
            return;
        }
        if (eDetails.getText().toString().isEmpty())
        {
            eDetails.setError("Enter Details");
            eDetails.requestFocus();
            return;
        }
        if (etYoutube.getText().toString().isEmpty())
        {
            etYoutube.setError("Error Link");
            etYoutube.requestFocus();
            return;
        }
        if (etBlogID.getText().toString().isEmpty())
        {
            etBlogID.setError("Enter Blog ID");
            etBlogID.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);
        eUpload.setEnabled(false);
        if (mimageUri != null)
        {
            Toast.makeText(MainActivity.this,"Please Wait...Uploading the blog",Toast.LENGTH_SHORT).show();
            StorageReference fileReference = mstorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mimageUri));
            fileReference.putFile(mimageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            Upload upload=new Upload(eTitle.getText().toString(),eDate.getText().toString(),
                                    etTime.getText().toString(),eTags.getText().toString(),
                                    eDetails.getText().toString(),taskSnapshot.getDownloadUrl().toString(),
                                    etYoutube.getText().toString(),etBlogID.getText().toString()

                            );
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            progressBar.setVisibility(View.GONE);
                            eUpload.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
        else {
            Toast.makeText(MainActivity.this,"No Image Choosen ", Toast.LENGTH_SHORT).show();
            eUpload.setEnabled(true);
            progressBar.setVisibility(View.GONE);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK
                && data!=null && data.getData()!=null) {

            mimageUri=data.getData();
            Picasso.with(this).load(mimageUri).into(eImageView);
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
