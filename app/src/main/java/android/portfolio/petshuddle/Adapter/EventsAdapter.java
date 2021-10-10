package android.portfolio.petshuddle.Adapter;

import android.content.Context;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> eventList;
    private Context context;

    public EventsAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView eventTitleItem;
        private TextView eventDateItem;
        private TextView eventLocationItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //implement setonclick here
            eventTitleItem = itemView.findViewById(R.id.eventTitleItem);
            eventDateItem = itemView.findViewById(R.id.eventDateItem);
            eventLocationItem = itemView.findViewById(R.id.eventLocationItem);
        }
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.event_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {

        Event event = eventList.get(position);

        TextView eventTitle = holder.eventTitleItem;
        eventTitle.setText(event.getEventTitle());
        TextView eventDate = holder.eventDateItem;
        eventDate.setText(event.getEventDate());
        TextView eventLocation = holder.eventLocationItem;
        eventLocation.setText(event.getEventLocation());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

}
