package br.unifor.gfinanceira.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {



    private lateinit var mRegisterFirstname: EditText;
    private lateinit var mRegisterLastName: EditText;
    private lateinit var mRegisterEmail: EditText;
    private lateinit var mRegisterPhone: EditText;
    private lateinit var mRegisterPassword: EditText;
    private lateinit var mRegisterPasswordConfirmation: EditText;
    private lateinit var mRegisterSignup: Button;

    private lateinit var mAuth: FirebaseAuth;
    private lateinit var mDatabase: FirebaseDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()

        mRegisterFirstname = findViewById(R.id.register_edittext_firstname);
        mRegisterLastName = findViewById(R.id.register_edittext_lastname);
        mRegisterEmail = findViewById(R.id.register_edittext_email);
        mRegisterPhone = findViewById(R.id.register_edittext_phone);
        mRegisterPassword = findViewById(R.id.register_edittext_password);
        mRegisterPasswordConfirmation = findViewById(R.id.register_edittext_passwordconfirm);

        mRegisterSignup = findViewById(R.id.register_button_signup);
        mRegisterSignup.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_button_signup -> {
                val firstname = mRegisterFirstname.text.toString();
                val lastname = mRegisterLastName.text.toString();
                val email = mRegisterEmail.text.toString();
                val phone = mRegisterPhone.text.toString();
                val password = mRegisterPassword.text.toString();
                val passwordconfirm = mRegisterPasswordConfirmation.text.toString();

                var isFormRightFilled = true;


                if (firstname.isEmpty()) {
                    mRegisterFirstname.error = getString(R.string.form_is_required_error)
                    isFormRightFilled = false;
                }
                if (lastname.isEmpty()) {
                    mRegisterLastName.error = getString(R.string.form_is_required_error);
                    isFormRightFilled = false;
                }
                if (email.isEmpty()) {
                    mRegisterEmail.error = getString(R.string.form_is_required_error);
                    isFormRightFilled = false;
                }
                if (phone.isEmpty()) {
                    mRegisterPhone.error = getString(R.string.form_is_required_error);
                    isFormRightFilled = false;
                }
                if (password.isEmpty()) {
                    mRegisterPassword.error = getString(R.string.form_is_required_error);
                    isFormRightFilled = false;
                }
                if(passwordconfirm.isEmpty()){
                    mRegisterPasswordConfirmation.error = getString(R.string.form_is_required_error);
                    isFormRightFilled = false;
                }

                if(isFormRightFilled){

                    if(password != passwordconfirm){
                        mRegisterPasswordConfirmation.error = "As senhas não são iguais"
                        return
                    }
                    val handler = Handler(Looper.getMainLooper())

                    val progress = ProgressDialog(this);
                    progress.setTitle("G Finanças - Aguarde")
                    progress.isIndeterminate = true;
                    progress.setCancelable(false);

                    progress.show();

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            progress.dismiss();
                        if(it.isSuccessful) {

                            val user = User(firstname, lastname, email, phone)

                            val ref = mDatabase.getReference("users/${mAuth.uid}");
                            ref.setValue(user);

                            handler.post {
                                Toast.makeText(
                                    applicationContext,
                                    "O usuário, ${firstname}, foi cadastrado com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                finish()
                            }
                        }
                        else {
                            handler.post{
                                Toast.makeText(
                                        applicationContext,
                                        it.exception?.message,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    };

                }

            }
        }
    }
}