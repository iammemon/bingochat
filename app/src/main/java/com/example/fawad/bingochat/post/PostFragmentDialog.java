package com.example.fawad.bingochat.post;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Fawad on 12/1/2017.
 */

public class PostFragmentDialog extends DialogFragment {

    private static final int PICK_IMAGE = 10;
    ImageView preview;
    ProgressBar progress;
    EditText titleEditText;
    EditText summaryEditText;
    Button uploadImageBtn;
    Uri ImageToBeUploaded;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.post_dialog,null);
        preview=(ImageView) view.findViewById(R.id.imagePreview);
        progress=(ProgressBar) view.findViewById(R.id.imageProgress);
        titleEditText=(EditText)view.findViewById(R.id.editTextTitle);
        summaryEditText=(EditText)view.findViewById(R.id.editTextSummary);
        uploadImageBtn=(Button) view.findViewById(R.id.uploadImageBtn);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview.setVisibility(View.VISIBLE);
                PickImageFromGallery();
            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setTitle("Add Post")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });

        builder.setView(view);
        return builder.create();
    }

    private void PickImageFromGallery(){
        final Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog postDialog=(AlertDialog)getDialog();
        if(postDialog!=null){
            final Button PositiveButton=(Button)postDialog.getButton(Dialog.BUTTON_POSITIVE);
            PositiveButton.setOnClickListener(new View.OnClickListener() {
                //when add button is clicked add post
                @Override
                public void onClick(View view) {
                    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    final String title=titleEditText.getText().toString();
                    final String summary=summaryEditText.getText().toString();
                    //first upload image
                    UploadTask task=Utils.UploadImage(ImageToBeUploaded,user.getUid());
                    //set all components to disable while operation is ongoing
                    PositiveButton.setEnabled(false);
                    uploadImageBtn.setEnabled(false);
                    titleEditText.setEnabled(false);
                    summaryEditText.setEnabled(false);
                    //if image is successfully uploaded then post data in database
                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Post post=new Post(user.getDisplayName(),
                                    user.getUid(),
                                    user.getEmail(),
                                    user.getPhotoUrl().toString(),
                                    title,
                                    summary,
                                    taskSnapshot.getDownloadUrl().toString());

                            Utils.WritePost(post,user.getUid(),getActivity().findViewById(R.id.cordinatorLayout));
                        }
                    });
                    //to track progress of image uploading
                    task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progressNum = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                            Log.e("info",progressNum+"");
                            progress.setIndeterminate(true);
                            if((int)progressNum==100){
                                postDialog.dismiss();
                            }

                        }
                    });
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when image is selected from gallery
        if(requestCode==PICK_IMAGE && resultCode== Activity.RESULT_OK){
            Uri img=data.getData();
            ImageToBeUploaded=img;
            preview.setImageURI(img);
        }
    }
}
