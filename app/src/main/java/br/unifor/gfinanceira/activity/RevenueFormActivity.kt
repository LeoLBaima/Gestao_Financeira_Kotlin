package br.unifor.gfinanceira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.model.Revenue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ReceitaFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mRevenueFormTitle:TextView;
    private lateinit var mRevenueFormName:EditText;
    private lateinit var mRevenueFormValue:EditText;
    private lateinit var mRevenueFormSave:Button;

    private var mUserID = -1;

    private lateinit var mRevenueId:String;

    private val handler = Handler(Looper.getMainLooper());

    private lateinit var mAuth: FirebaseAuth;
    private lateinit var mDatabase: FirebaseDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita)

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()


        mRevenueId = intent.getStringExtra("revenueId") ?: ""

        mRevenueFormTitle = findViewById(R.id.revenue_form_textview_title);
        mRevenueFormName = findViewById(R.id.revenue_form_edittext_name);
        mRevenueFormValue = findViewById(R.id.revenue_form_edittext_value);

        mRevenueFormSave = findViewById(R.id.revenue_form_button_save);
        mRevenueFormSave.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.revenue_form_button_save -> {

                    val name = mRevenueFormName.text.toString();
                    val value = mRevenueFormValue.text.toString();

                    var isFormRightFilled = true;

                    if(name.isEmpty()) {
                        mRevenueFormName.error = "Este campo não pode ser vazio"
                        isFormRightFilled = false
                    }

                    if(value.isEmpty()){
                        mRevenueFormValue.error = "Este campo não pode ser vazio"
                        isFormRightFilled = false
                    }

                    if(isFormRightFilled){

                        if(mRevenueId.isEmpty()){
                            val rid = mDatabase.reference.child("users/${mAuth.uid}/revenues").push().key
                            val revenue = Revenue(rid!!, name, value.toInt())
                            val ref = mDatabase.getReference("users/${mAuth.uid}/revenues/$rid")
                            ref.setValue(revenue);

                            handler.post {
                                Toast.makeText(
                                    applicationContext,
                                    "Tarefa, ${revenue.name}, adicionada com sucesso",
                                    Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }
                    }
                else {

                }
            }
        }
    }
}