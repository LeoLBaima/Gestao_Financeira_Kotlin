package br.unifor.gfinanceira.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.gfinanceira.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mLoginEmail:EditText;
    private lateinit var mLoginPassword: EditText;
    private lateinit var mLoginSignin: Button;
    private lateinit var mRegister:TextView;

    private lateinit var mAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        mAuth = FirebaseAuth.getInstance();

        mLoginEmail = findViewById(R.id.login_edittext_email);
        mLoginPassword = findViewById(R.id.login_edittext_password);

        mLoginSignin = findViewById(R.id.login_button_signin);
        mLoginSignin.setOnClickListener(this);



        mRegister = findViewById(R.id.login_textview_register);
        mRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_textview_register -> {
                val it = Intent(this, RegisterActivity::class.java)
                startActivity(it);
            }

            R.id.login_button_signin -> {
                val email = mLoginEmail.text.toString();
                val password = mLoginPassword.text.toString();

                var isLoginFormFilled = true;

                if(email.isEmpty()){
                    mLoginEmail.error = "Este campo está vázio"
                    isLoginFormFilled = false;
                }
                if(password.isEmpty()){
                    mLoginPassword.error = "Este campo está vázio"
                    isLoginFormFilled = false;
                }

                if(isLoginFormFilled){

                    val progress = ProgressDialog(this);
                    progress.setTitle("G Finanças - Aguarde")
                    progress.isIndeterminate = true;
                    progress.setCancelable(false);

                    progress.show();


                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            progress.dismiss();
                            if(it.isSuccessful) {
                                val it = Intent(applicationContext, MainActivity::class.java)
                                startActivity(it);
                                finish();
                            }
                            else {
                                showLoginErrorMessage();
                            }
                    }


//                    GlobalScope.launch {
//
//                        val user = userDAO.findByEmail(email);
//
//                        if(user != null) {
//
//                            if(BCrypt.verifyer().verify(password.toCharArray(), user.pass).verified){
//
//                                val it = Intent(applicationContext, MainActivity::class.java)
//                                it.putExtra("userId", user.uid);
//                                startActivity(it);
//                                finish();
//                            }
//                            else {
//                                showLoginErrorMessage()
//                            }
//
//                        }
//                        else {
//                            showLoginErrorMessage()
//                        }
//
//                    }


                }


            }

        }
    }

    private fun showLoginErrorMessage() {
        val handler = Handler(Looper.getMainLooper())
        handler.post{
            Toast.makeText(
                    applicationContext,
                    "Usuário ou senha inválidos",
                    Toast.LENGTH_SHORT).show();
        }
    }

}