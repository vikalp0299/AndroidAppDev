package com.example.navigationbar.view;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.navigationbar.FirstTeamActivity;
import com.example.navigationbar.R;
import com.example.navigationbar.fragments.TeamsFragment;
import com.example.navigationbar.model.TeamData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public final class TeamAdapter extends Adapter {

    private final Context c;
    //Context context;
    private final ArrayList teamList;

    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new TeamViewHolder(v);
    }

    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Object obj = this.teamList.get(position);
        TeamData newList = (TeamData)obj;
        holder.getName().setText((CharSequence)newList.getTeamName());
        holder.getDetail().setText((CharSequence)newList.getTeamDetail());
    }

    public void onBindViewHolder(ViewHolder vh, int i) {
        this.onBindViewHolder((TeamViewHolder)vh, i);
    }

    public int getItemCount() {
        return this.teamList.size();
    }


    public final Context getC() {
        return this.c;
    }


    public final ArrayList getTeamList() {
        return this.teamList;
    }

    public TeamAdapter(Context c, ArrayList teamList) {
        super();
        this.c = c;
        this.teamList = teamList;
    }

    public final class TeamViewHolder extends ViewHolder {

        private TextView name;

        private TextView detail;

        private ImageView mMenus;

        private LinearLayout tile;

        private final View v;


        public final TextView getName() {
            return this.name;
        }

        public final void setName( TextView textView) {
            this.name = textView;
        }


        public final TextView getDetail() {
            return this.detail;
        }

        public final void setDetail(TextView textView) {
            this.detail = textView;
        }


        public final ImageView getMMenus() {
            return this.mMenus;
        }

        public final void setMMenus( ImageView imageView) {
            this.mMenus = imageView;
        }

        private final void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = TeamAdapter.this.getTeamList().get(this.getAdapterPosition());
            final TeamData position = (TeamData)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);
            popupMenus.inflate(R.menu.show_menu);
            popupMenus.setOnMenuItemClickListener((OnMenuItemClickListener)(new OnMenuItemClickListener() {
                public final boolean onMenuItemClick(MenuItem it) {
                    boolean bool;
                    switch(it.getItemId()) {
                        case R.id.editText:
                            View v = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_item, (ViewGroup)null);
                            final EditText name = (EditText)v.findViewById(R.id.teamName);
                            final EditText number = (EditText)v.findViewById(R.id.teamDetails);
                            (new Builder(getV().getContext())).setView(v).setPositiveButton((CharSequence)"Ok", (OnClickListener)(new OnClickListener() {
                                public final void onClick(DialogInterface dialog, int $noName_1) {
                                    TeamData pos = position;
                                    EditText editText = name;
                                    pos.setTeamName(editText.getText().toString());
                                    pos = position;
                                    editText = number;
                                    pos.setTeamDetail(editText.getText().toString());
                                    TeamAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getV().getContext(), (CharSequence)"Team Information is Edited", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })).setNegativeButton((CharSequence)"Cancel", (OnClickListener)null).create().show();
                            bool = true;
                            break;
                        case R.id.delete:
                            (new Builder(getV().getContext())).setTitle((CharSequence)"Delete").setIcon(R.drawable.ic_warning).setMessage((CharSequence)"Are you sure delete this Team").setPositiveButton((CharSequence)"Yes", (OnClickListener)(new OnClickListener() {
                                public final void onClick(DialogInterface dialog, int $noName_1) {
                                    TeamAdapter.this.getTeamList().remove(TeamViewHolder.this.getAdapterPosition());
                                    TeamAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(getV().getContext(), (CharSequence)"Deleted this Team", Toast.LENGTH_SHORT).show();
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

        public TeamViewHolder(View v) {
            super(v);
            this.v = v;
            View view = this.v.findViewById(R.id.mTitle);
            this.name = (TextView)view;
            view = this.v.findViewById(R.id.mSubTitle);
            this.detail = (TextView)view;
            view = this.v.findViewById(R.id.mMenus);
            this.mMenus = (ImageView)view;
            view = this.v.findViewById(R.id.tile);
            this.tile = (LinearLayout)view;
            this.mMenus.setOnClickListener(new android.view.View.OnClickListener() {
                public final void onClick(View it) {
                    TeamViewHolder viewHolder = TeamViewHolder.this;
                    try {
                        viewHolder.popupMenus(it);
                        Log.d("boom", getTeamList().toString());
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
            this.tile.setOnClickListener(new android.view.View.OnClickListener(){
                public final void onClick(View it){
                    onItemClick(getAdapterPosition());
                    Log.d("boom",getTeamList().toString());
                }
            });
        }
        public void onItemClick(int position){

            final Intent intent;
            /*switch (position){
                case 0:
                    intent =  new Intent(getV().getContext(), FirstTeamActivity.class);
                    break;
                default:
                    intent = new Intent(getV().getContext(), TeamsFragment.class);
                    break;
            }*/
            intent =  new Intent(getV().getContext(), FirstTeamActivity.class);
            getV().getContext().startActivity(intent);

        }

    }
}
