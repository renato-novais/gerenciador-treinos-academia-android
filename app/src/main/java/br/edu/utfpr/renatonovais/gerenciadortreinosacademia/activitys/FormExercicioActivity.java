package br.edu.utfpr.renatonovais.gerenciadortreinosacademia.activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.R;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.Exercicio;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Regiao;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.entidades.enums.Tipo;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.persistencia.ExercicioDatabase;
import br.edu.utfpr.renatonovais.gerenciadortreinosacademia.utils.UtilsGUI;

public class FormExercicioActivity extends AppCompatActivity {

    public static final String MODO = "MODO";

    public static final String ID = "ID";

    public static final int NOVO = 1;
    public static final int ALTERAR = 2;

    public static final String SUGERIR_TIPO = "SUGERIR_TIPO";
    public static final String ULTIMO_TIPO = "ULTIMO_TIPO";

    private boolean sugerirTipo = false;
    private int ultimoTipo = 0;

    private EditText editTextNome;
    private RadioGroup radioGroupTipo;
    private Spinner spinnerRegiao;
    private CheckBox checkBoxRequerAnilhasHalteres;
    private int modo;
    private Exercicio exercicioOriginal;

    public static void novoExercicio(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(activity, FormExercicioActivity.class);
        intent.putExtra(MODO, NOVO);
        launcher.launch(intent);
    }

    public static void alterarExercicio(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher, Exercicio exercicio) {
        Intent intent = new Intent(activity, FormExercicioActivity.class);
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, exercicio.getId());

        launcher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextNome = findViewById(R.id.editTextNome);
        radioGroupTipo = findViewById(R.id.radioGroupTipo);
        spinnerRegiao = findViewById(R.id.spinnerRegiao);
        checkBoxRequerAnilhasHalteres = findViewById(R.id.checkBoxRequerAnilhasHalteres);
        popularSpinnerRegiao();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        lerSugerirTipo();
        lerUltimoTipo();

        if (bundle != null) {

            modo = bundle.getInt(MODO, NOVO);

            if (modo == NOVO) {
                setTitle(getString(R.string.novo_exercicio));
                if(sugerirTipo) {
                    if (ultimoTipo == 0) {
                        radioGroupTipo.check(R.id.radioButtonTipoCardio);
                    }
                    if (ultimoTipo == 1) {
                        radioGroupTipo.check(R.id.radioButtonTipoMusculacao);
                    }
                }
            } else {
                setTitle(getString(R.string.alterar_exercicio));

                long id = bundle.getLong(ID);

                ExercicioDatabase database = ExercicioDatabase.getDatabase(this);

                exercicioOriginal = database.getExercicioDao().queryForId(id);
                editTextNome.setText(exercicioOriginal.getNome());
                editTextNome.setSelection(editTextNome.getText().length());

                int tipoOrdinal = exercicioOriginal.getTipo().ordinal();
                if (tipoOrdinal == 0) {
                    radioGroupTipo.check(R.id.radioButtonTipoCardio);
                }
                if (tipoOrdinal == 1) {
                    radioGroupTipo.check(R.id.radioButtonTipoMusculacao);
                }

                spinnerRegiao.setSelection(exercicioOriginal.getRegiao().ordinal() + 1);
                checkBoxRequerAnilhasHalteres.setChecked(exercicioOriginal.isRequerAnilhaOuHalter());
            }
        }
        editTextNome.requestFocus();
    }

    public void salvarCampos() {
        String nome = editTextNome.getText().toString();
        if (nome == null || nome.trim().isEmpty()) {
            UtilsGUI.aviso(this, R.string.erro_nome);
            editTextNome.requestFocus();
            return;
        }

        int tipoId = radioGroupTipo.getCheckedRadioButtonId();
        Tipo tipo;

        if (R.id.radioButtonTipoMusculacao == tipoId) {
            tipo = Tipo.Musculacao;
        } else if (R.id.radioButtonTipoCardio == tipoId) {
            tipo = Tipo.Cardio;
        } else {
            UtilsGUI.aviso(this, R.string.erro_tipo);
            radioGroupTipo.requestFocus();
            return;
        }

        String regiaoString = (String) spinnerRegiao.getSelectedItem();

        if (regiaoString == null) {
            UtilsGUI.aviso(this, R.string.erro_regiao);
            return;
        }
        if (regiaoString.isEmpty()) {
            UtilsGUI.aviso(this, R.string.erro_regiao);
            return;
        }

        boolean requerAnilhaHalter = checkBoxRequerAnilhasHalteres.isChecked();

        if(modo == ALTERAR
        && nome.equals(exercicioOriginal.getNome())
        && tipo == exercicioOriginal.getTipo()
        && regiaoString == exercicioOriginal.getRegiao().name()
        && requerAnilhaHalter == exercicioOriginal.isRequerAnilhaOuHalter()
        ) {
            cancelar();
            return;
        }

        salvarUltimoTipo(tipo.ordinal());

        Intent intent = new Intent();
        ExercicioDatabase database = ExercicioDatabase.getDatabase(this);

        if (modo == NOVO){
            Exercicio exercicio = new Exercicio(nome, tipo, Regiao.fromString(regiaoString), requerAnilhaHalter);
            long novoId = database.getExercicioDao().insert(exercicio);

            if (novoId <= 0){
                UtilsGUI.aviso(this, R.string.erro_ao_tentar_inserir);
                return;
            }

            exercicio.setId(novoId);
            intent.putExtra(ID, exercicio.getId());

        }else{

            Exercicio exercicioAlterado = new Exercicio(nome, tipo, Regiao.fromString(regiaoString), requerAnilhaHalter);
            exercicioAlterado.setId(exercicioOriginal.getId());

            int quantidadeAlterada = database.getExercicioDao().update(exercicioAlterado);

            if (quantidadeAlterada == 0){
                UtilsGUI.aviso(this, R.string.erro_ao_tentar_alterar);
                return;
            }

            intent.putExtra(ID, exercicioAlterado.getId());
        }

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void limparCampos () {
        limparNome();
        desmarcarTipo();
        desmarcarRequerAnilhasHalteres();
        desmarcarRegiao();
        UtilsGUI.aviso(this, R.string.aviso_campo_limpo);
    }

    private void limparNome() {
        editTextNome.setText(null);
    }

    private void desmarcarTipo() {
        radioGroupTipo.clearCheck();
    }

    private void popularSpinnerRegiao(){
        ArrayList<String> lista = new ArrayList<>();
        lista.add(getString(R.string.vazio));
        lista.add(getString(R.string.peito));
        lista.add(getString(R.string.costas));
        lista.add(getString(R.string.ombros));
        lista.add(getString(R.string.bracos));
        lista.add(getString(R.string.pernas));
        lista.add(getString(R.string.abdomen));
        lista.add(getString(R.string.aerobica));

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        lista);
        spinnerRegiao.setAdapter(adapter);
    }

    private void desmarcarRequerAnilhasHalteres(){
        checkBoxRequerAnilhasHalteres.setChecked(false);
    }

    private void desmarcarRegiao(){
        spinnerRegiao.setSelection(0);
    }

    public void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_exercicio_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);
        item.setChecked(sugerirTipo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idMenuItem = item.getItemId();

        if(idMenuItem == R.id.menuItemSalvar) {
            salvarCampos();
            return true;
        }
        if(idMenuItem == R.id.menuItemLimpar) {
            limparCampos();
            return true;
        }
        else {
            if(idMenuItem == R.id.menuItemSugerirTipo) {
                boolean valor = !item.isChecked();
                salvarSugerirTipo(valor);
                item.setChecked(valor);
                if(sugerirTipo) {
                    if (ultimoTipo == 0) {
                        radioGroupTipo.check(R.id.radioButtonTipoCardio);
                    }
                    if (ultimoTipo == 1) {
                        radioGroupTipo.check(R.id.radioButtonTipoMusculacao);
                    }
                }
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void lerSugerirTipo() {
        SharedPreferences shared = getSharedPreferences(ListagemExercicioActivity.ARQUIVO, Context.MODE_PRIVATE);
        sugerirTipo = shared.getBoolean(SUGERIR_TIPO,sugerirTipo);
    }

    private void salvarSugerirTipo(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ListagemExercicioActivity.ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(SUGERIR_TIPO, novoValor);
        editor.commit();
        sugerirTipo = novoValor;
    }

    private void lerUltimoTipo() {
        SharedPreferences shared = getSharedPreferences(ListagemExercicioActivity.ARQUIVO, Context.MODE_PRIVATE);
        ultimoTipo = shared.getInt(ULTIMO_TIPO, ultimoTipo);
    }

    private void salvarUltimoTipo(int novoValor) {
        SharedPreferences shared = getSharedPreferences(ListagemExercicioActivity.ARQUIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(ULTIMO_TIPO, novoValor);
        editor.commit();
        ultimoTipo = novoValor;
    }
}