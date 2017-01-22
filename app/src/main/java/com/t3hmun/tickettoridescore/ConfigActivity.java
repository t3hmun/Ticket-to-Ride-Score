package com.t3hmun.tickettoridescore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.HashMap;

public class ConfigActivity extends AppCompatActivity {

    private final String BUNDLE_CONF = "conf";

    ConfigData data;
    private HashMap<GameEdition, RadioButton> opts;
    private HashMap<Colours, CheckBox> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        RadioButton usaOption = (RadioButton) findViewById(R.id.usaOption);
        RadioButton euroOption = (RadioButton) findViewById(R.id.europeOption);

        // Not using group because it makes the mapping to enum easier.
        opts = new HashMap<>();
        opts.put(GameEdition.USA, usaOption);
        opts.put(GameEdition.EURO, euroOption);

        CheckBox redCheck = (CheckBox) findViewById(R.id.redCheck);
        CheckBox yellowCheck = (CheckBox) findViewById(R.id.yellowCheck);
        CheckBox greenCheck = (CheckBox) findViewById(R.id.greenCheck);
        CheckBox blueCheck = (CheckBox) findViewById(R.id.blueCheck);
        CheckBox blackCheck = (CheckBox) findViewById(R.id.blackCheck);

        players = new HashMap<>();
        players.put(Colours.RED, redCheck);
        players.put(Colours.YELLOW, yellowCheck);
        players.put(Colours.GREEN, greenCheck);
        players.put(Colours.BLUE, blueCheck);
        players.put(Colours.BLACK, blackCheck);

        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(BUNDLE_CONF);

        } else {
            data = new ConfigData();
        }

        for (HashMap.Entry<GameEdition, RadioButton> entry : opts.entrySet()) {
            final GameEdition edition = entry.getKey();
            entry.getValue().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setGameEdition(edition);
                }
            });
        }

        for (HashMap.Entry<Colours, CheckBox> entry : players.entrySet()) {
            final Colours colour = entry.getKey();
            entry.getValue().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.getPlayers().put(colour, isChecked);
                }
            });
        }

        // Pointless but meh nvm.
        UpdateUI();
    }


    /**
     * I wrote this before I realised that view had a saveEnabled feature.
     * So it turns out most views can save themselves so I don't have to bundle everything.
     * My first project was full of textViews that needed bundling.
     */
    private void UpdateUI() {
        assert data != null;
        opts.get(data.getGameEdition()).setChecked(true);

        for (HashMap.Entry<Colours, Boolean> entry : data.getPlayers().entrySet()) {
            players.get(entry.getKey()).setChecked(entry.getValue());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_CONF, data);
        super.onSaveInstanceState(outState);
    }
}
