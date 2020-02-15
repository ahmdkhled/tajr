package com.greyeg.tajr.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;
//
//import com.esafirm.imagepicker.features.ImagePicker;
//import com.esafirm.imagepicker.model.Image;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.greyeg.tajr.R;
import com.greyeg.tajr.adapters.MentionAdapter;
import com.greyeg.tajr.adapters.MessageAdapter;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.Message;
import com.greyeg.tajr.models.User;
import com.greyeg.tajr.view.utils.AudioRecordView;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.rygelouv.audiosensei.player.AudioSenseiListObserver;

import com.yalantis.ucrop.UCrop;

import net.gotev.speech.Speech;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

public class ChatActivity extends AppCompatActivity implements AudioRecordView.RecordingListener {


    public static Activity context;
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    SessionManager sessionManager;
//
//    @BindView(R.id.float_send)
//    DrawMeImageButton drawMeButton;
//
//    @BindView(R.id.progress)
//    SpeechProgressView speechProgressView;
//
//    @BindView(R.id.btn_record)
//    DrawMeImageButton btn_record;
//
//    @BindView(R.id.linearLayout)
//    LinearLayout speatchLayout;
//
//    @BindView(R.id.message_input)
//    EditText messageInput;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.friend_chat_recycler_view)
    RecyclerView messagesRecyclerView;

    List<Message> messages;
    MessageAdapter adapter;
    LinearLayoutManager messagesLinearLayoutManager;

    private Bitmap thumbBitmap = null;

    private static final int FIRST_REQUEST = 0;
    private static final int SECOND_REQUEST = 1;
    UCrop.Options options;
    private byte[] imageBytes;

    private AudioRecordView audioRecordView;
    private List<User> users = new ArrayList<>();

    public static View mentionView;

    @BindView(R.id.users)
    RecyclerView usersRecyclerView;
    LinearLayoutManager mentionLinearLayoutManager;
    public static MentionAdapter mentionAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        context = this;
        sessionManager=SessionManager.getInstance(getApplicationContext());
        AudioSenseiListObserver.getInstance().registerLifecycle(getLifecycle());
        Speech.init(this, getPackageName());
        getMessages();
        initViews();
        mentionView = findViewById(R.id.mentionView);
        audioRecordView = findViewById(R.id.recordingView);


        audioRecordView.setRecordingListener(this);

        setListener();
    }

    private void setListener() {
        mentionLinearLayoutManager = new LinearLayoutManager(this);
        mentionAdapter = new MentionAdapter(users, ChatActivity.this);
        usersRecyclerView.setLayoutManager(mentionLinearLayoutManager);
        usersRecyclerView.setAdapter(mentionAdapter);
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    mentionAdapter.addUsers(user);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        audioRecordView.getMessageView().setHint(R.string.wrong_message);
        audioRecordView.getAttachmentView().setOnClickListener(v ->
                ImagePicker.create(ChatActivity.this)
                        .limit(1)
                        .theme(R.style.UCrop)
                        .folderMode(false)
                        .start()
        );

        audioRecordView.getSendView().setOnClickListener(v -> sendMessage());

    }

    public static void showUsersToMention( ) {

        animationView(mentionView);

    }

    public static void filterRecucler(String name){
        mentionAdapter.filter(name);
    }

    public static void hideUsersToMention() {
        animationViewHide(mentionView);
    }

    public static void animationView(View view) {
        view.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.bottom_sheet_fad_in);
        view.startAnimation(anim);
        mentionView.setVisibility(View.VISIBLE);
    }

    public static void animationViewHide(View view) {
        view.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.bottom_sheet_fad_out);
        view.startAnimation(anim);
        mentionView.setVisibility(View.GONE);
    }


    @Override
    public void onRecordingStarted() {
        showToast("started");
        debug("started");

        startRecording();
    }

    @Override
    public void onRecordingLocked() {
        showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        showToast("completed");
        debug("completed");
        stopRecording(false);
    }

    @Override
    public void onRecordingCanceled() {
        showToast("canceled");
        debug("canceled");
        stopRecording(true);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void debug(String log) {
        Log.d("VarunJohn", log);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Created by:\nVarun John\nvarunjohn1990@gmail.com\n\nCheck code on Github :\nhttps://github.com/varunjohn/Audio-Recording-Animation")
                .setCancelable(false)
                .setPositiveButton("Github", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://github.com/varunjohn/Audio-Recording-Animation";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            i.setPackage(null);
                            try {
                                startActivity(i);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        setUpProgressBar();
        options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
//        drawMeButton.setImageResource(R.drawable.ic_send_image_white);
//        drawMeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btn_record.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    startRecording();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    speatchLayout.setVisibility(View.GONE);
//                    stopRecording();
//                }
//                return true;
//            }
//        });
//
//        btn_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("", "onClick: ");
//            }
//        });
//
//        messageInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    messageInput.setGravity(Gravity.START);
//                    drawMeButton.setImageResource(R.drawable.ic_send_white);
//                    btn_record.setVisibility(View.GONE);
//                    drawMeButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            sendMessage();
//
//
//                        }
//                    });
//                } else {
//                    messageInput.setGravity(Gravity.CENTER);
//                    drawMeButton.setImageResource(R.drawable.ic_send_image_white);
//                    btn_record.setVisibility(View.VISIBLE);
//                    drawMeButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ImagePicker.create(ChatActivity.this)
//                                    .limit(1)
//                                    .theme(R.style.UCrop)
//                                    .folderMode(false)
//                                    .start();
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
    }

    private void getMessages() {
        messages = new ArrayList<>();
        messagesLinearLayoutManager = new LinearLayoutManager(this);
        adapter = new MessageAdapter(messages, this);
        messagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        messagesLinearLayoutManager.setStackFromEnd(true);
        // messagesLinearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setReverseLayout(true);
        messagesLinearLayoutManager.setSmoothScrollbarEnabled(true);
        messagesRecyclerView.setHasFixedSize(true);
        //messagesRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        messagesRecyclerView.setLayoutManager(messagesLinearLayoutManager);

        messagesRecyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!dataSnapshot.hasChildren()) {
                    progressBar.setVisibility(View.GONE);
                }
                if (dataSnapshot.getValue() != null) {
                    Message message = dataSnapshot.getValue(Message.class);
//                    adapter.add(message);
//                    adapter.notifyDataSetChanged();
                    messages.add(message);
                    //  messageKey.add(dataSnapshot.getKey());
                    adapter.notifyItemInserted(messages.size() - 1);
                    messagesRecyclerView.scrollToPosition(messages.size() - 1);
                    progressBar.setVisibility(View.GONE);
                } else {

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {

        String message = audioRecordView.getMessageView().getText().toString();

        MainActivity.sendNotification(message);
        //sendNotification(message);
        Long time = System.currentTimeMillis();
        String userNeme = sessionManager.getUserName();
        String usetId = sessionManager.getUserId();
        final String messageKey = FirebaseDatabase.getInstance().getReference().push().getKey();
        String imageUri = "";
        String recordUri = "";
        String type = "msg";

        final Message message1 = new Message(
                userNeme, usetId, message, type, time, imageUri, messageKey, recordUri
        );
        FirebaseDatabase.getInstance().getReference().child("messages").child(messageKey).setValue(message1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "onComplete: ");

                        }
                    }
                });

        audioRecordView.getMessageView().setText("");
    }

    long startTimeRecording;
    long endTimeRecording;

    private void startRecording() {

        startTimeRecording = System.currentTimeMillis();
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/" + System.currentTimeMillis() + ".3gp";
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();


        } catch (Exception e) {
            Log.d("gggggggggggg", "startRecording: " + e.getMessage());
        }

    }

    private void stopRecording(boolean cancele) {
        if (cancele) {
            try {
                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
            } catch (Exception e) {
                Log.d("gggggggggggg", "startstopniging: " + e.getMessage());

            }
            return;
        }
        if ((startTimeRecording + 1000) <= System.currentTimeMillis()) {
            startTimeRecording = 0;
            try {
                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                sendRecordMessage();
            } catch (Exception e) {
                Log.d("gggggggggggg", "startstopniging: " + e.getMessage());

            }
        } else {
            Toast.makeText(this, "يجب ان يكون التسجيل اكثر من ثانية", Toast.LENGTH_SHORT).show();
            startTimeRecording = 0;
            try {

                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
            } catch (Exception e) {
                Log.d("gggggggggggg", "startstopniging: " + e.getMessage());

            }
        }

    }

//
//
//    private void onRecord(boolean start) {
//        if (start) {
//            startRecording();
//        } else {
//            stopRecording();
//        }
//    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void sendRecordMessage() {

        final StorageReference thumbFilePathRef = FirebaseStorage.getInstance().getReference().
                child(sessionManager.getUserId())
                .child("recorders").child(mFileName);

        thumbFilePathRef.putFile(Uri.fromFile(new File(mFileName).getAbsoluteFile()))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        thumbFilePathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri thumbUri) {

                                String message = audioRecordView.getMessageView().getText().toString();
                                Long time = System.currentTimeMillis();
                                String userNeme = sessionManager.getUserName();
                                String usetId = sessionManager.getUserId();
                                final String messageKey = FirebaseDatabase.getInstance().getReference().push().getKey();
                                String imageUri = "";
                                String recordUri = String.valueOf(thumbUri);
                                String type = "record";
                                MainActivity.sendNotification("تسجيل صوتى");
                                Message message1 = new Message(
                                        userNeme, usetId, message, type, time, imageUri, messageKey, recordUri
                                );
                                FirebaseDatabase.getInstance().getReference().child("messages").child(messageKey).setValue(message1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseDatabase.getInstance().getReference().child("messages").child(messageKey).
                                                            child("seen").child(sessionManager.getUserId())
                                                            .setValue(sessionManager.getUserId())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(ChatActivity.this, "تم ارسال المقطع الصوتى", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });

                                //  myProfileImage.setImageURI(thumbUri);
                            }
                        });
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //   Speech.getInstance().shutdown();

    }
//
//    private void RunAnimation() {
//        DrawMeImageButton tv = btn_record;
//        if (drawMeButton.getVisibility() == View.VISIBLE) {
//            Animation a = AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_fad_out);
//            a.reset();
//            tv.clearAnimation();
//            tv.startAnimation(a);
//            tv.setVisibility(View.GONE);
//
//        } else {
//            Animation a = AnimationUtils.loadAnimation(this, R.anim.bottom_sheet_fad_in);
//            a.reset();
//            tv.clearAnimation();
//            tv.startAnimation(a);
//            tv.setVisibility(View.VISIBLE);
//
//        }
//
//    }

    void setUpProgressBar() {
        int[] colors = new int[4];
        colors[0] = getResources().getColor(R.color.red);
        colors[1] = getResources().getColor(R.color.blue);
        colors[2] = getResources().getColor(R.color.yellow);
        colors[3] = getResources().getColor(R.color.green);
        Drawable progressDrawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(colors)
                .build();
        Rect bounds = progressBar.getIndeterminateDrawable().getBounds();
        progressBar.setIndeterminateDrawable(progressDrawable);
        progressBar.getIndeterminateDrawable().setBounds(bounds);

    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            //Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            return;
        }
        String destinationFileName = "SAMPLE_CROPPED_IMAGE_NAME" + ".jpg";

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);
            Uri res_url = Uri.fromFile(new File((image.getPath())));
            CropImage(image, res_url);

        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            //  if (resultUri!=null)
            assert resultUri != null;
            bitmapCompress(resultUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();
            uploadThumbImage(imageBytes);
            Log.d("TAG", "onActivityResult: " + Arrays.toString(imageBytes));
        }

    }

    private void CropImage(Image image, Uri res_url) {
        UCrop.of(res_url, Uri.fromFile(new File(getCacheDir(), image.getName())))
                .withOptions(options)
                .start(ChatActivity.this);
    }

    private void bitmapCompress(Uri resultUri) {
        final File thumbFilepathUri = new File(resultUri.getPath());

        try {
            thumbBitmap = new Compressor(this)
                    .setQuality(50)
                    .compressToBitmap(thumbFilepathUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //upload thumb image
    private void uploadThumbImage(byte[] thumbByte) {
        final StorageReference thumbFilePathRef = FirebaseStorage.getInstance().getReference().
                child(sessionManager.getUserId())
                .child("chat_images").child(System.currentTimeMillis() + ".jpg");
        thumbFilePathRef.putBytes(thumbByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                thumbFilePathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri thumbUri) {

                        sendMessage(String.valueOf(thumbUri));
                        //  myProfileImage.setImageURI(thumbUri);
                    }
                });
            }
        });
    }

    private void sendMessage(String s) {
        String message = audioRecordView.getMessageView().getText().toString();
        Long time = System.currentTimeMillis();
        String userNeme = sessionManager.getUserName();
        String usetId = sessionManager.getUserId();
        final String messageKey = FirebaseDatabase.getInstance().getReference().push().getKey();
        String imageUri = s;
        String recordUri = "";
        String type = "img";
        MainActivity.sendNotificationImage();
        Message message1 = new Message(
                userNeme, usetId, message, type, time, imageUri, messageKey, recordUri
        );
        FirebaseDatabase.getInstance().getReference().child("messages").child(messageKey).setValue(message1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "onComplete: ");
                        }
                    }
                });

    }

    public void closeMentionView(View view) {
        hideUsersToMention();
    }
}
