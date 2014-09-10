package com.example.nfcp2p_api10;

import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AndroidBeamMainActivity extends Activity {

	

	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_android_beam);
		
		mNfcAdapter = mNfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()), 0);
	}


	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences userInfo = getSharedPreferences("Card", 0);  
        String CardID = userInfo.getString("CardID", "");    
		if(CardID.equals("")){
			Toast.makeText(this, "ø®∫≈Œ™ø’,«Îœ»…Ë÷√ø®∫≈", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (mNfcAdapter != null)
		{
			NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{createTextRecord(CardID)});
			mNfcAdapter.enableForegroundNdefPush(this, ndefMessage); 
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mNfcAdapter != null)
			mNfcAdapter.disableForegroundNdefPush(this);
	}
	public NdefRecord createTextRecord(String text) {
		byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		Charset utfEncoding = Charset.forName("UTF-8");
		byte[] textBytes = text.getBytes(utfEncoding);
		int utfBit = 0;
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], data);

		return record;
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.setCardNumber:
            Intent serverIntent = new Intent(this, SetCardNumber.class);
            startActivity(serverIntent);
            return true;
        case R.id.MainActivy:
            return true;
        }
        return false;
    }

}
