package com.friendlyphire.compass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.PasswordViewHolder> {

    class PasswordViewHolder extends RecyclerView.ViewHolder{
        private final TextView passwordItemView;

        private PasswordViewHolder(View itemView) {
            super(itemView);
            passwordItemView = itemView.findViewById(R.id.pwtv);
        }
    }

    private final LayoutInflater mInflater;
    private List<Password> mPasswords; // Cached copy of words
    private PasswordManagerActivity context;
    PasswordListAdapter(PasswordManagerActivity context) { mInflater = LayoutInflater.from(context); this.context=context;}

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PasswordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PasswordViewHolder holder, int position) {
        if (mPasswords != null) {
            final Password current = mPasswords.get(position);
            final TextView mView = holder.passwordItemView;
            mView.setText(current.getHint());
            mView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mView.getText().toString().equals(current.getHint())){
                                mView.setText(current.getPassword());
                                mView.setBackgroundColor(Color.parseColor("#ff757575"));
                                mView.setBackground(context.getDrawable(R.drawable.textbackflip));
                            }
                            else{
                                mView.setText(current.getHint());
                                mView.setBackgroundColor(Color.parseColor("#ff303030"));
                                mView.setBackground(context.getDrawable(R.drawable.textback));
                            }
                        }
                    }
            );
            holder.passwordItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            context.deletePassword(current);
                        }};
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure you wish to delete this entry:\nHint: "+current.getHint()+"\nPassword: "+current.getPassword());
                    alertDialogBuilder.setPositiveButton("Delete",posListener);
                    alertDialogBuilder.setNegativeButton("Back",null);
                    alertDialogBuilder.create().show();
                    return true;
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.passwordItemView.setText("No Passwords");
        }
    }

    void setPasswords(List<Password> passwords){
        mPasswords = passwords;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPasswords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPasswords != null)
            return mPasswords.size();
        else return 0;
    }
}
