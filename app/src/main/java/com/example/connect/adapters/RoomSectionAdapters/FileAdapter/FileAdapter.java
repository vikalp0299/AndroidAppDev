package com.example.connect.adapters.RoomSectionAdapters.FileAdapter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.connect.model.File;
import com.example.connect.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public final class FileAdapter extends Adapter {

    private final Context c;
    private ArrayList<File> fileList;

    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.file_item, parent, false);

        return new FileViewHolder(v);
    }

    public void onBindViewHolder(FileViewHolder holder, int position) {
        Object obj = this.fileList.get(position);
        File newList = (File)obj;
        holder.getName().setText(newList.getFileName());
        holder.getImage().setImageResource(newList.getFileImgId());
    }

    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        this.onBindViewHolder((FileViewHolder)vh, i);
    }

    public int getItemCount() {
        return this.fileList.size();
    }

    public void setFilter(ArrayList<File> fileModels){
        fileList = new ArrayList<>();
        fileList.addAll(fileModels);
        notifyDataSetChanged();
    }

    public final ArrayList<File> getFileList() {
        return this.fileList;
    }

    public FileAdapter(Context c, ArrayList<File> fileList) {
        super();
        this.c = c;
        this.fileList = fileList;
    }

    public final class FileViewHolder extends ViewHolder {

        private TextView name;
        private ImageView image;
        private ImageView mMenus;
        //private LinearLayout memTile;
        private final View v;

        public final TextView getName() {
            return this.name;
        }
        public final ImageView getImage() {
            return this.image;
        }

        private void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = FileAdapter.this.getFileList().get(this.getAdapterPosition());
            final File position = (File)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);

            popupMenus.inflate(R.menu.show_menu);
            popupMenus.setOnMenuItemClickListener(it -> {
                boolean bool;
                switch(it.getItemId()) {
                    case R.id.editText:
                        View v1 = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_file_item, null);
                        final EditText memName = v1.findViewById(R.id.fileTitle);
                        (new Builder(getV().getContext())).setView(v1).setPositiveButton("Ok", (dialog, $noName_1) -> {
                            File pos = position;
                            EditText editText = memName;
                            String names = editText.getText().toString();
                            if(names.equals("")){
                                Toast.makeText(getV().getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                pos.setFileName(names);
                                FileAdapter.this.notifyDataSetChanged();
                                Toast.makeText(getV().getContext(), "File Information is Edited", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }).setNegativeButton("Cancel", null).create().show();
                        bool = true;
                        break;
                    case R.id.delete:
                        (new Builder(getV().getContext())).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure delete this File").setPositiveButton("Yes", (dialog, $noName_1) -> {
                            FileAdapter.this.getFileList().remove(FileViewHolder.this.getAdapterPosition());
                            FileAdapter.this.notifyDataSetChanged();
                            Toast.makeText(getV().getContext(), "Deleted this File", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }).setNegativeButton("No", null).create().show();
                        bool = true;
                        break;
                    default:
                        bool = true;
                }

                return bool;
            });
            popupMenus.show();
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menu = popup.get(popupMenus);
            assert menu != null;
            menu.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(menu, true);
        }


        public final View getV() {
            return this.v;
        }

        public FileViewHolder(View v) {
            super(v);
            this.v = v;
            View view = this.v.findViewById(R.id.fileName);
            this.name = (TextView)view;
            view = this.v.findViewById(R.id.fileImage);
            this.image = (ImageView)view;
            view = this.v.findViewById(R.id.mMenus);
            this.mMenus = (ImageView)view;
            this.mMenus.setOnClickListener(it -> {
                FileViewHolder viewHolder = FileViewHolder.this;
                try {
                    viewHolder.popupMenus(it);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
