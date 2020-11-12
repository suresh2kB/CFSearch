package com.example.cfsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CodeAdapter extends ArrayAdapter<Code> {

    private static final String LOCATION_SEPARATOR = " of ";

    public CodeAdapter(@NonNull Context context, List<Code> code) {
        super(context, 0,code);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.list_item,parent,false);
        }
        Code currentCode = getItem(position);

        TextView firstName = listItemView.findViewById(R.id.first_name_json);
        firstName.setText(currentCode.getmFirstName());

        TextView lastName = listItemView.findViewById(R.id.last_name_json);
        lastName.setText(currentCode.getmLastName());

        TextView currentRating = listItemView.findViewById(R.id.current_reting_json);
        currentRating.setText(currentCode.getmCurrentRating());

        TextView maxRating = listItemView.findViewById(R.id.max_rating_json);
        maxRating.setText(currentCode.getmMaxRating());

        TextView lastSeen = listItemView.findViewById(R.id.last_time_online_json);
        lastSeen.setText(currentCode.getmLastSeen());

        TextView regTime = listItemView.findViewById(R.id.registered_time_json);
        regTime.setText(currentCode.getmRegTime());

        TextView contributions = listItemView.findViewById(R.id.contribution_json);
        contributions.setText(currentCode.getmContribution());

        TextView totalFriends = listItemView.findViewById(R.id.friends_json);
        totalFriends.setText(currentCode.getmTotalFriends());

        TextView currentRank = listItemView.findViewById(R.id.current_rank_json);
        currentRank.setText(currentCode.getmCurrentRank());

        TextView maxRank = listItemView.findViewById(R.id.max_rank_json);
        maxRank.setText(currentCode.getmMaxRank());
        return listItemView;
    }

}
