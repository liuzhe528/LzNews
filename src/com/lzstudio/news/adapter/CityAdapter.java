
package com.lzstudio.news.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.lzstudio.news.wedget.city.ContactItemInterface;
import com.lzstudio.news.wedget.city.ContactListAdapter;

public class CityAdapter extends ContactListAdapter
{

    public CityAdapter(Context _context, int _resource,
            List<ContactItemInterface> _items)
    {
        super(_context, _resource, _items);
    }

    @Override
    public void populateDataForRow(View parentView, ContactItemInterface item,
            int position)
    {
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView nicknameView = (TextView) infoView
                .findViewById(R.id.cityName);

        nicknameView.setText(item.getDisplayInfo());
    }

}
