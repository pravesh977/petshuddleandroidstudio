package android.portfolio.petshuddle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.portfolio.petshuddle.Entity.Event;
import android.portfolio.petshuddle.R;
import android.portfolio.petshuddle.UI.SingleEventScreen;
import android.portfolio.petshuddle.UI.SinglePetScreen;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Event currentEvent = eventList.get(position);
                    Intent intent = new Intent(context, SingleEventScreen.class);
                    intent.putExtra("eventId", currentEvent.getEventId());
                    intent.putExtra("eventTitle", currentEvent.getEventTitle());
                    intent.putExtra("eventDetails", currentEvent.getEventDetails());
                    intent.putExtra("eventLocation", currentEvent.getEventLocation());
                    intent.putExtra("eventDateTime", currentEvent.getEventDate());
                    intent.putExtra("userId", currentEvent.getUserId());
                    context.startActivity(intent);
                }
            });
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
