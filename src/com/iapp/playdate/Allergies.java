package com.iapp.playdate;

import com.iapp.playdate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Allergies extends Activity implements OnCheckedChangeListener {
	
	CheckBox chk_1,chk_2,chk_3,chk_4,chk_5,chk_6,chk_7,chk_8,chk_9,chk_10,chk_11,chk_12,chk_13,chk_14,chk_15;
	String allergies_selected="";
	Button submit;
	EditText edit_other;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allergies);
		submit=(Button)findViewById(R.id.button1_allergies);
		edit_other=(EditText)findViewById(R.id.editText_allergies_xml);
		chk_1=(CheckBox)findViewById(R.id.checkBox1_1);
		chk_2=(CheckBox)findViewById(R.id.checkBox1_2);
		chk_3=(CheckBox)findViewById(R.id.checkBox1_3);
		chk_4=(CheckBox)findViewById(R.id.checkBox1_4);
		chk_5=(CheckBox)findViewById(R.id.checkBox1_5);
		chk_6=(CheckBox)findViewById(R.id.checkBox1_6);
		chk_7=(CheckBox)findViewById(R.id.checkBox1_7);
		chk_8=(CheckBox)findViewById(R.id.checkBox1_8);
		chk_9=(CheckBox)findViewById(R.id.checkBox1_9);
		chk_10=(CheckBox)findViewById(R.id.checkBox1_10);
		chk_11=(CheckBox)findViewById(R.id.checkBox1_11);
		chk_12=(CheckBox)findViewById(R.id.checkBox1_12);
		chk_13=(CheckBox)findViewById(R.id.checkBox1_13);
		chk_14=(CheckBox)findViewById(R.id.checkBox1_14);
		chk_15=(CheckBox)findViewById(R.id.checkBox1_15);
		chk_1.setOnCheckedChangeListener(this);
		chk_2.setOnCheckedChangeListener(this);
		chk_3.setOnCheckedChangeListener(this);
		chk_4.setOnCheckedChangeListener(this);
		chk_5.setOnCheckedChangeListener(this);
		chk_6.setOnCheckedChangeListener(this);
		chk_7.setOnCheckedChangeListener(this);
		chk_8.setOnCheckedChangeListener(this);
		chk_9.setOnCheckedChangeListener(this);
		chk_10.setOnCheckedChangeListener(this);
		chk_11.setOnCheckedChangeListener(this);
		chk_12.setOnCheckedChangeListener(this);
		chk_13.setOnCheckedChangeListener(this);
		chk_14.setOnCheckedChangeListener(this);
		chk_15.setOnCheckedChangeListener(this);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("allergies_selected");
				String other=edit_other.getText().toString();
				if(other.equals("")||other.equals(null)){
					
				}
				else{
					allergies_selected=allergies_selected+other;
				}
				Toast.makeText(Allergies.this,allergies_selected,5000).show();
				
			}
		});
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.checkBox1_1:
			if(allergies_selected.equals("")){
				allergies_selected="Nutmilk";
			}
			else{
			allergies_selected=allergies_selected+","+"Nutmilk";
			}
			break;

case R.id.checkBox1_2:
	if(allergies_selected.equals("")){
		allergies_selected="Egg";
	}
	else{
	allergies_selected=allergies_selected+","+"Egg";
	}
	break;
case R.id.checkBox1_3:
	if(allergies_selected.equals("")){
		allergies_selected="Wheat";
	}
	else{
	allergies_selected=allergies_selected+","+"Wheat";
	}
	break;
case R.id.checkBox1_4:
	if(allergies_selected.equals("")){
		allergies_selected="SoyFish";
	}
	else{
	allergies_selected=allergies_selected+","+"SoyFish";
	}
	break;
case R.id.checkBox1_5:
	if(allergies_selected.equals("")){
		allergies_selected="Corn";
	}
	else{
	allergies_selected=allergies_selected+","+"Corn";
	}
	break;
case R.id.checkBox1_6:
	if(allergies_selected.equals("")){
		allergies_selected="GelatinMeat";
	}
	else{
	allergies_selected=allergies_selected+","+"GelatinMeat";
	}
	break;
case R.id.checkBox1_7:
	if(allergies_selected.equals("")){
		allergies_selected="Seeds";
	}
	else{
	allergies_selected=allergies_selected+","+"Seeds";
	}
	break;
case R.id.checkBox1_8:
	if(allergies_selected.equals("")){
		allergies_selected="Spices";
	}
	else{
	allergies_selected=allergies_selected+","+"Spices";
	}
	break;
case R.id.checkBox1_9:
	if(allergies_selected.equals("")){
		allergies_selected="Grass";
	}
	else{
	allergies_selected=allergies_selected+","+"Grass";
	}
	break;
case R.id.checkBox1_10:
	if(allergies_selected.equals("")){
		allergies_selected="Banana";
	}
	else{
	allergies_selected=allergies_selected+","+"Banana";
	}
	break;
case R.id.checkBox1_11:
	if(allergies_selected.equals("")){
		allergies_selected="Dairy Anaphy laxis";
	}
	else{
	allergies_selected=allergies_selected+","+"Dairy Anaphy laxis";
	}
	break;
case R.id.checkBox1_12:
	if(allergies_selected.equals("")){
		allergies_selected="Hay feaver Insect";
	}
	else{
	allergies_selected=allergies_selected+","+"Hay feaver Insect";
	}
	break;
case R.id.checkBox1_13:
	if(allergies_selected.equals("")){
		allergies_selected="Insect";
	}
	else{
	allergies_selected=allergies_selected+","+"Insect";
	}
	break;
case R.id.checkBox1_14:
	if(allergies_selected.equals("")){
		allergies_selected="Stings Lactose";
	}
	else{
	allergies_selected=allergies_selected+","+"Stings Lactose";
	}
	break;
case R.id.checkBox1_15:
	if(allergies_selected.equals("")){
		allergies_selected="CeliacGluten";
	}
	else{
	allergies_selected=allergies_selected+","+"CeliacGluten";
	}
	break;

		
		}
	}

}
