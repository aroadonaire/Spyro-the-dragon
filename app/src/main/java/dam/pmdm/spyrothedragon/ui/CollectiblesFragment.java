package dam.pmdm.spyrothedragon.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.adapters.CollectiblesAdapter;
import dam.pmdm.spyrothedragon.databinding.FragmentCollectiblesBinding;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesFragment extends Fragment {

    private FragmentCollectiblesBinding binding;
    private RecyclerView recyclerView;
    private CollectiblesAdapter adapter;
    private List<Collectible> collectiblesList;
    private TextView guideBubbleCollectibles;
    private int gemTapCount = 0;
    private VideoView videoView;
    private FrameLayout gemContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectiblesBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerViewCollectibles;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        collectiblesList = new ArrayList<>();
        adapter = new CollectiblesAdapter(collectiblesList);
        recyclerView.setAdapter(adapter);

        loadCollectibles();
        guideBubbleCollectibles = binding.getRoot().findViewById(R.id.guide_bubble_text_collectibles);

        videoView = binding.getRoot().findViewById(R.id.videoView);
        gemContainer = binding.getRoot().findViewById(R.id.gemContainer);
        gemContainer.setVisibility(View.VISIBLE);
        ImageView gemImageView = binding.getRoot().findViewById(R.id.gemImageView);

        gemImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gemTapCount++; // Incrementar el contador de toques
                    if (gemTapCount == 4) {
                        activateEasterEgg();
                        gemTapCount = 0; // Reiniciar el contador después de activar el Easter Egg
                    }
                }
                return true;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadCollectibles() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.collectibles);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            Collectible currentCollectible = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if ("collectible".equals(tagName)) {
                            currentCollectible = new Collectible();
                        } else if (currentCollectible != null) {
                            if ("name".equals(tagName)) {
                                currentCollectible.setName(parser.nextText());
                            } else if ("description".equals(tagName)) {
                                currentCollectible.setDescription(parser.nextText());
                            } else if ("image".equals(tagName)) {
                                currentCollectible.setImage(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();

                        if ("collectible".equals(tagName) && currentCollectible != null) {
                            collectiblesList.add(currentCollectible);
                        }
                        break;
                }

                eventType = parser.next();
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showGuideBubble() {
        guideBubbleCollectibles.setVisibility(View.VISIBLE);

        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        guideBubbleCollectibles.startAnimation(fadeIn);

        guideBubbleCollectibles.postDelayed(new Runnable() {
            @Override
            public void run() {
                guideBubbleCollectibles.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void activateEasterEgg() {
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.easter_egg_video));
        videoView.start();

        videoView.setOnCompletionListener(mediaPlayer -> {
            Toast.makeText(getContext(), "¡Easter Egg activado!", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction()
                    .replace(R.id.gemContainer, new CollectiblesFragment())
                    .commit();
        });
    }
}