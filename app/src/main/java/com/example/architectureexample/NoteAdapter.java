package com.example.architectureexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {
    //initialize a member variable for the list of notes.

    private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note>DIFF_CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription())&&
                    oldItem.getPriority()==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Here we will have to create and return a note holder.
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        //its from here we get the data from the single note java objects into the views of the note holder.
        //now we need a reference to he note object at this position.
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));

    }




    //To get the Note from the Adapter to the outside.
    public Note getNoteAt(int position) {
        return getItem(position);
    }

    //create a reference for calling methods from this adapter onto the OnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    //to get the click event on to the MainActivity create an interface.
    public interface OnItemClickListener {
        //In an interface you dont provide the implementation of a method,you just declare the method.
        void onItemClick(Note note);
    }

    //create viewholder class that will hold the views in the senoir recyclerviewitems.
    class NoteHolder extends RecyclerView.ViewHolder {
        //create variable s for the different views
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            //to catch a click anywhere when we click the cardview(itemView).
            //to get the click event togeteher with the clicked note onto the main activity.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //add the buff listener to the itemClick.
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }

                }
            });
        }
    }
}
