package com.example.navigationbar.view;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.navigationbar.R;
import com.example.navigationbar.model.UserData;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public final class UserAdapter extends Adapter {

    private final Context c;
    private final ArrayList userList;

    public UserAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new UserAdapter.UserViewHolder(v);
    }

    public void onBindViewHolder(UserAdapter.UserViewHolder holder, int position) {
        Object obj = this.userList.get(position);
        UserData newList = (UserData)obj;
        holder.getName().setText((CharSequence)newList.getUserName());
        holder.getMbNum().setText((CharSequence)newList.getUserMb());
    }

    public void onBindViewHolder(ViewHolder vh, int i) {
        this.onBindViewHolder((UserAdapter.UserViewHolder)vh, i);
    }

    public int getItemCount() {
        return this.userList.size();
    }


    public final Context getC() {
        return this.c;
    }


    public final ArrayList getUserList() {
        return this.userList;
    }

    public UserAdapter( Context c,  ArrayList userList) {
        super();
        this.c = c;
        this.userList = userList;
    }

    public final class UserViewHolder extends ViewHolder {

        private TextView name;

        private TextView mbNum;

        private ImageView mMenus;

        private final View v;


        public final TextView getName() {
            return this.name;
        }

        public final void setName( TextView textView) {
            this.name = textView;
        }


        public final TextView getMbNum() {
            return this.mbNum;
        }

        public final void setMbNum( TextView textView) {
            this.mbNum = textView;
        }


        public final ImageView getMMenus() {
            return this.mMenus;
        }

        public final void setMMenus( ImageView imageView) {
            this.mMenus = imageView;
        }

        private final void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = UserAdapter.this.getUserList().get(this.getAdapterPosition());
            final UserData position = (UserData)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);
            popupMenus.inflate(R.menu.show_menu);
            popupMenus.setOnMenuItemClickListener((OnMenuItemClickListener)(new OnMenuItemClickListener() {
                public final boolean onMenuItemClick(MenuItem it) {
                    boolean bool;
                    switch(it.getItemId()) {
                        case R.id.editText:
                            View v = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_item, (ViewGroup)null);
                            final EditText name = (EditText)v.findViewById(R.id.userName);
                            final EditText number = (EditText)v.findViewById(R.id.userNo);
                            (new Builder(getV().getContext())).setView(v).setPositiveButton((CharSequence)"Ok", (OnClickListener)(new OnClickListener() {
                                public final void onClick(DialogInterface dialog, int $noName_1) {
                                    UserData pos = position;
                                    EditText editText = name;
                                    pos.setUserName(editText.getText().toString());
                                    pos = position;
                                    editText = number;
                                    pos.setUserMb(editText.getText().toString());
                                    UserAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getV().getContext(), (CharSequence)"User Information is Edited", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })).setNegativeButton((CharSequence)"Cancel", (OnClickListener)null).create().show();
                            bool = true;
                            break;
                        case R.id.delete:
                            (new Builder(getV().getContext())).setTitle((CharSequence)"Delete").setIcon(R.drawable.ic_warning).setMessage((CharSequence)"Are you sure delete this Information").setPositiveButton((CharSequence)"Yes", (OnClickListener)(new OnClickListener() {
                                public final void onClick(DialogInterface dialog, int $noName_1) {
                                    UserAdapter.this.getUserList().remove(UserViewHolder.this.getAdapterPosition());
                                    UserAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getV().getContext(), (CharSequence)"Deleted this Information", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })).setNegativeButton((CharSequence)"No", (OnClickListener)null).create().show();
                            bool = true;
                            break;
                        default:
                            bool = true;
                    }

                    return bool;
                }
            }));
            popupMenus.show();
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menu = popup.get(popupMenus);
            menu.getClass().getDeclaredMethod("setForceShowIcon", Boolean.TYPE).invoke(menu, true);
        }


        public final View getV() {
            return this.v;
        }

        public UserViewHolder( View v) {
            super(v);
            this.v = v;
            View view = this.v.findViewById(R.id.mTitle);
            this.name = (TextView)view;
            view = this.v.findViewById(R.id.mSubTitle);
            this.mbNum = (TextView)view;
            view = this.v.findViewById(R.id.mMenus);
            this.mMenus = (ImageView)view;
            this.mMenus.setOnClickListener(new android.view.View.OnClickListener() {
                public final void onClick(View it) {
                    UserAdapter.UserViewHolder viewHolder = UserViewHolder.this;
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
                }
            });
        }
    }
}
