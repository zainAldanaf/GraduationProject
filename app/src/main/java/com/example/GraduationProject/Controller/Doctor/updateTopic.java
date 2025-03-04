package com.example.GraduationProject.Controller.Doctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.GraduationProject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class updateTopic extends AppCompatActivity {
    ImageView imageView;
    VideoView videoView;
    Button Choosevideo;
    Uri videouri;
    MediaController mediaController;
    Button chooseimage;
    EditText address;
    EditText cotent;

    Uri imageUri;
    StorageReference storageReference;
    StorageReference storageReference2;
   Button edit;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_topic);
        progressDialog=new ProgressDialog(this);
        imageView=findViewById(R.id.image_update);
        chooseimage=findViewById(R.id.edit_choose_image);
        videoView=findViewById(R.id.editvideoView);
        Choosevideo=findViewById(R.id.editchoose_video);
        videoView.setMediaController(mediaController);
        videoView.start();

        firebaseFirestore=FirebaseFirestore.getInstance();
        edit=findViewById(R.id.update_btn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String id= getIntent().getStringExtra("id");
                UpdateTopic(id.toString());
            }
        });

        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Choosevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
            }
        });
       
    }

    private void UpdateTopic(String id) {
        // Ensure that the content and address fields are initialized
        cotent = findViewById(R.id.edittopic_details);
        address = findViewById(R.id.edittopic_address);

        // First, upload the video if it exists
        if (videouri != null) {
            uploadVideo(videouri, id);
        } else {
            // If no video is selected, proceed with just the image update
            uploadImageAndUpdateFirestore(id, null);
        }
    }

    // Method to upload video and get the video URL
    private void uploadVideo(Uri videoUri, final String id) {
        String filename = "video_" + System.currentTimeMillis() + ".mp4";
        StorageReference videoStorageReference = FirebaseStorage.getInstance().getReference("videos/" + filename);

        videoStorageReference.putFile(videoUri)
                .addOnSuccessListener(taskSnapshot -> videoStorageReference.getDownloadUrl()
                        .addOnSuccessListener(videoUrl -> {
                            // Once video upload is complete, upload the image
                            uploadImageAndUpdateFirestore(id, videoUrl.toString());
                        }))
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error uploading video", e);
                    Toast.makeText(updateTopic.this, "Video upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    // Method to upload image and update Firestore
    private void uploadImageAndUpdateFirestore(String id, String videoUrl) {
        if (imageUri != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_s", Locale.CANADA);
            String filename = simpleDateFormat.format(new Date()) + ".png";
            StorageReference imageStorageReference = FirebaseStorage.getInstance().getReference("images/" + filename);

            imageStorageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageStorageReference.getDownloadUrl()
                            .addOnSuccessListener(imageUrl -> {
                                // Once image upload is complete, update Firestore document
                                updateTopicInFirestore(id, imageUrl.toString(), videoUrl);
                            }))
                    .addOnFailureListener(e -> {
                        Log.w("TAG", "Error uploading image", e);
                        Toast.makeText(updateTopic.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Method to update Firestore with image, video, and topic details
    private void updateTopicInFirestore(String id, String imageUrl, String videoUrl) {
        firebaseFirestore.collection("Topics").document(id)
                .update(
                        "topic_title", address.getText().toString(),
                        "topic_content", cotent.getText().toString(),
                        "topic_image", imageUrl,
                        "topic_video", videoUrl != null ? videoUrl : "" // If no video, pass empty string
                )
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "DocumentSnapshot successfully updated!");
                    Toast.makeText(updateTopic.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    // Go back to home or list screen
                    Intent intent = new Intent(updateTopic.this, DoctorHome.class); // Adjust the activity name as needed
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error updating document", e);
                    Toast.makeText(updateTopic.this, "Failed to update topic", Toast.LENGTH_SHORT).show();
                });
    }

//    private void UpdateTopic(String id) {
//        cotent=findViewById(R.id.edittopic_details);
//        address=findViewById(R.id.edittopic_address);
//        storageReference= FirebaseStorage.getInstance().getReference("videos.mp4/");
//
//        storageReference.getDownloadUrl().addOnSuccessListener(videoUri -> {
//            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_s", Locale.CANADA);
//            Date date=new Date();
//            String filename= simpleDateFormat.format(date);
//            storageReference= FirebaseStorage.getInstance().getReference("images.png/"+filename);
//            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    imageView.setImageURI(null);
//
//                    storageReference.getDownloadUrl().addOnSuccessListener(imageUri-> {
//
//                        firebaseFirestore.collection("Topics").document(id).update("topic_title", address.getText().toString(),
//                                      "topic_content",  cotent.getText().toString(),
//                                        "topic_image",imageUri.toString(),
//                                        "topic_video",videoUri.toString()
//                                        )
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.d("aaa", "DocumentSnapshot successfully updated!");
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w("aaaa", "Error updating document", e);
//                                    }
//                                });
//                    });
//
//                    Toast.makeText(updateTopic.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(updateTopic.this, "failed", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        });
//    }

    public  void selectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData()!= null){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);

        }else if (requestCode == 101 && data != null && data.getData()!= null){
            videouri=data.getData();
            videoView.setVideoURI(videouri);
        }
    }

}