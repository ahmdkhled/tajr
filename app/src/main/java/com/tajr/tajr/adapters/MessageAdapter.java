package com.tajr.tajr.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tajr.tajr.R;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.models.Message;
import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM_FORM = 3;
    private final int VIEW_TYPE_ITEM_FROM_ME = 0;
    private final int VIEW_TYPE_ITEM_TO_ME = 2;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Message> mMessagesList;

    private Activity context;

    public MessageAdapter(List<Message> mMessagesList, Activity context) {
        this.mMessagesList = mMessagesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessagesList.get(position);

        String id = message.getUserId();

        if (!id.equals(SessionManager.getInstance(context).getUserId())) {
            return VIEW_TYPE_ITEM_TO_ME;
        } else {
            return VIEW_TYPE_ITEM_FROM_ME;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM_FROM_ME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_from_me, parent, false);
            return new MessageFromMeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_to_me, parent, false);
            return new MessageToMeViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Message message = mMessagesList.get(position);
        if (holder instanceof MessageFromMeViewHolder) {
            final MessageFromMeViewHolder fromMeViewHolder = (MessageFromMeViewHolder) holder;
            if (message.getType().equals("msg")) {
                fromMeViewHolder.audio_player.setVisibility(View.GONE);
                fromMeViewHolder.messageImage.setVisibility(View.GONE);
                fromMeViewHolder.messageText.setText(message.getMessage());
                PrettyTime p = new PrettyTime();
                Date date = new Date(message.getTime());
                fromMeViewHolder.messageTime.setText(p.format(date));
                fromMeViewHolder.messageText.setVisibility(View.VISIBLE);
            } else if (message.getType().equals("record")) {
                fromMeViewHolder.audio_player.setVisibility(View.VISIBLE);
                fromMeViewHolder.audio_player.setAudioTarget(message.getRecordUrl());
                fromMeViewHolder.messageImage.setVisibility(View.GONE);
                PrettyTime p = new PrettyTime();
                fromMeViewHolder.messageText.setVisibility(View.GONE);
                Date date = new Date(message.getTime());
                fromMeViewHolder.messageTime.setText(p.format(date));
            } else {
                fromMeViewHolder.audio_player.setVisibility(View.GONE);
                final String[] photo = new String[]{message.getImageUrl()};
                fromMeViewHolder.messageImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder<>(context, photo)
                                .setStartPosition(0)
                                .show();
                    }
                });
                fromMeViewHolder.messageImage.setImageURI(message.getImageUrl());
                fromMeViewHolder.messageImage.setVisibility(View.VISIBLE);
                PrettyTime p = new PrettyTime();
                Date date = new Date(message.getTime());
                fromMeViewHolder.messageTime.setText(p.format(date));
                fromMeViewHolder.messageText.setVisibility(View.GONE);
            }
            fromMeViewHolder.userName.setText(message.getUserName());
//            if (position-1>-1&&mMessagesList.get(position-1).getUserId().equals(message.getUserId())){
//                fromMeViewHolder.userName.setVisibility(View.GONE);
//            }else {
//
//            }


            fromMeViewHolder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fromMeViewHolder.messageTime.setVisibility(View.VISIBLE);

                }
            });

        } else if (holder instanceof MessageToMeViewHolder) {
            final MessageToMeViewHolder toMeViewHolder = (MessageToMeViewHolder) holder;
            if (message.getType().equals("msg")) {
                toMeViewHolder.audio_player.setVisibility(View.GONE);
                toMeViewHolder.messageImage.setVisibility(View.GONE);
                toMeViewHolder.messageText.setText(message.getMessage());
                PrettyTime p = new PrettyTime();
                Date date = new Date(message.getTime());
                toMeViewHolder.messageTime.setText(p.format(date));
                toMeViewHolder.messageText.setVisibility(View.VISIBLE);
            } else if (message.getType().equals("record")) {
                toMeViewHolder.audio_player.setVisibility(View.VISIBLE);
                toMeViewHolder.audio_player.setAudioTarget(message.getRecordUrl());
                toMeViewHolder.messageImage.setVisibility(View.GONE);
                toMeViewHolder.messageText.setVisibility(View.GONE);
                PrettyTime p = new PrettyTime();
                Date date = new Date(message.getTime());
                toMeViewHolder.messageTime.setText(p.format(date));
            } else {
                final String[] photo = new String[]{message.getImageUrl()};
                toMeViewHolder.messageImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder<>(context, photo)
                                .setStartPosition(0)
                                .show();
                    }
                });
                toMeViewHolder.audio_player.setVisibility(View.GONE);
                toMeViewHolder.messageImage.setImageURI(message.getImageUrl());
                toMeViewHolder.messageImage.setVisibility(View.VISIBLE);
                toMeViewHolder.messageText.setVisibility(View.GONE);
                PrettyTime p = new PrettyTime();
                Date date = new Date(message.getTime());
                toMeViewHolder.messageTime.setText(p.format(date));
            }

            toMeViewHolder.userName.setText(message.getUserName());


            toMeViewHolder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toMeViewHolder.messageTime.setVisibility(View.VISIBLE);

                }
            });
        }

    }


    public void add(Message message) {
        mMessagesList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public class MessageFromMeViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView messageTime;
        TextView messageText;
        SimpleDraweeView messageImage;
        AudioSenseiPlayerView audio_player;
        View main;

        public MessageFromMeViewHolder(View itemView) {
            super(itemView);
            main = itemView;
            userName = itemView.findViewById(R.id.client_name_label);
            messageText = itemView.findViewById(R.id.message_text_view_of_message);
            messageImage = itemView.findViewById(R.id.photo_image_view_of_message_from_me);
            messageTime = itemView.findViewById(R.id.message_time_from_me);
            audio_player = itemView.findViewById(R.id.audio_player);
        }
    }

    public class MessageToMeViewHolder extends RecyclerView.ViewHolder {
        TextView messageTime;
        TextView messageText;
        SimpleDraweeView messageImage;
        TextView userName;
        View main;
        AudioSenseiPlayerView audio_player;

        public MessageToMeViewHolder(View itemView) {
            super(itemView);
            main = itemView;
            audio_player = itemView.findViewById(R.id.audio_player);
            userName = itemView.findViewById(R.id.client_name_label);
            messageTime = itemView.findViewById(R.id.message_time_to_me);
            messageImage = itemView.findViewById(R.id.photo_image_view_of_message_to_me);
            messageText = itemView.findViewById(R.id.message_text_view_of_message_tome);

        }
    }

}
