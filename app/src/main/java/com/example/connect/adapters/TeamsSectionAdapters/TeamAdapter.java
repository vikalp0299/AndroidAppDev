package com.example.connect.adapters.TeamsSectionAdapters;

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

import com.example.connect.TeamActivity;
import com.example.connect.R;
import com.example.connect.model.Team;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public final class TeamAdapter extends Adapter {

    private final Context c;
    private ArrayList<Team> teamList;

    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item, parent, false);
        return new TeamViewHolder(v);
    }

    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Object obj = this.teamList.get(position);
        Team newList = (Team)obj;
        holder.getName().setText(newList.getTeamName());
        holder.getDetail().setText(newList.getTeamDetail());
    }

    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        this.onBindViewHolder((TeamViewHolder)vh, i);
    }

    public int getItemCount() {
        return this.teamList.size();
    }

    public void setFilter(ArrayList<Team> countryModels){
        teamList = new ArrayList<>();
        teamList.addAll(countryModels);
        notifyDataSetChanged();
    }

    public final ArrayList<Team> getTeamList() {
        return this.teamList;
    }

    public TeamAdapter(Context c, ArrayList<Team> teamList) {
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
        public final TextView getDetail() {
            return this.detail;
        }

        private void popupMenus(View v) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object obj = TeamAdapter.this.getTeamList().get(this.getAdapterPosition());
            final Team position = (Team)obj;
            PopupMenu popupMenus = new PopupMenu(getV().getContext(), v);
            popupMenus.inflate(R.menu.show_menu);
            popupMenus.setOnMenuItemClickListener(it -> {
                boolean bool;
                switch(it.getItemId()) {
                    case R.id.editText:
                        View v1 = LayoutInflater.from(getV().getContext()).inflate(R.layout.add_item, null);
                        final EditText teamName = v1.findViewById(R.id.teamName);
                        final EditText teamDetail = v1.findViewById(R.id.teamDetails);
                        (new Builder(getV().getContext())).setView(v1).setPositiveButton("Ok", (dialog, $noName_1) -> {
                            Team pos = position;
                            EditText editText = teamName;
                            String names = editText.getText().toString();
                            editText = teamDetail;
                            String details = editText.getText().toString();
                            if(names.equals("") || details.equals("")){
                                Toast.makeText(getV().getContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                pos.setTeamName(names);
                                pos.setTeamDetail(details);
                                TeamAdapter.this.notifyDataSetChanged();
                                Toast.makeText(getV().getContext(), "Team Information is Edited", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }).setNegativeButton("Cancel", null).create().show();
                        bool = true;
                        break;
                    case R.id.delete:
                        (new Builder(getV().getContext())).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure delete this Team").setPositiveButton("Yes", (dialog, $noName_1) -> {
                            TeamAdapter.this.getTeamList().remove(TeamViewHolder.this.getAdapterPosition());
                            TeamAdapter.this.notifyDataSetChanged();
                            Toast.makeText(getV().getContext(), "Deleted this Team", Toast.LENGTH_SHORT).show();
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
            this.mMenus.setOnClickListener(it -> {
                TeamViewHolder viewHolder = TeamViewHolder.this;
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
            this.tile.setOnClickListener(it -> onItemClick());
        }

        public void onItemClick(){
            final Intent intent;
            intent =  new Intent(getV().getContext(), TeamActivity.class);
            getV().getContext().startActivity(intent);
        }
    }
}
