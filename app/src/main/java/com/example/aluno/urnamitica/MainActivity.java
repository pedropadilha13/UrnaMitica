package com.example.aluno.urnamitica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText inputQuantidade;
    Button btnIniciar;
    ImageView fotoLisa;
    ImageView fotoJisoo;
    Button btnBranco;
    TextView nomeLisa;
    TextView nomeJisoo;
    ProgressBar progressoVotacao;
    TextView textoProgresso;
    Button btnCorrige;
    Button btnConfirma;
    TextView textoBranco;
    ProgressBar barraVotosLisa;
    ProgressBar barraVotosJisoo;
    TextView textoResultadoLisa;
    TextView textoResultadoJisoo;
    TextView resultadoFinal;

    int quantidadeEleitores;
    int votosLisa;
    int votosJisoo;
    int votosBrancos;
    int eleitorAtual;

    ESCOLHA escolha;

    enum ESCOLHA {LISA, JISOO, BRANCO};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputQuantidade = findViewById(R.id.inputQuantidade);
        btnIniciar = findViewById(R.id.btnIniciar);
        fotoLisa = findViewById(R.id.lisa);
        fotoJisoo = findViewById(R.id.jisoo);
        btnBranco = findViewById(R.id.btnBranco);
        nomeLisa = findViewById(R.id.nomeLisa);
        nomeJisoo = findViewById(R.id.nomeJisoo);
        progressoVotacao = findViewById(R.id.progressoVotacao);
        textoProgresso = findViewById(R.id.textoProgresso);
        btnCorrige = findViewById(R.id.btnCorrige);
        btnConfirma = findViewById(R.id.btnConfirma);
        textoBranco = findViewById(R.id.textBranco);
        barraVotosLisa = findViewById(R.id.resultadoLisa);
        barraVotosJisoo = findViewById(R.id.resultadoJisoo);
        textoResultadoLisa = findViewById(R.id.textoResultadoLisa);
        textoResultadoJisoo = findViewById(R.id.textoResultadoJisoo);
        resultadoFinal = findViewById(R.id.textoResultadoFinal);

        votosLisa = 0;
        votosJisoo = 0;
        votosBrancos = 0;
        eleitorAtual = 1;

    }

    public void iniciarVotacao(View v) {
        String quantidadeEleitoresS = inputQuantidade.getText().toString();
        if (!quantidadeEleitoresS.equals("")) {
            quantidadeEleitores = Integer.valueOf(quantidadeEleitoresS);
            if (quantidadeEleitores < 3) {
                Toast.makeText(this, getString(R.string.numero_invalido), Toast.LENGTH_SHORT).show();
            } else {
                inputQuantidade.setVisibility(View.INVISIBLE);
                btnIniciar.setVisibility(View.INVISIBLE);
                montaTelaPrincipal();
            }
        } else {
            Toast.makeText(this, getString(R.string.input_vazio), Toast.LENGTH_SHORT).show();
        }
    }

    private void montaTelaPrincipal() {
        montaTelaPrincipal(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
    }

    private void montaTelaPrincipal(int stateLisa, int stateJisoo, int stateBranco, int stateBarra) {
        fotoLisa.setVisibility(stateLisa);
        nomeLisa.setVisibility(stateLisa);
        fotoJisoo.setVisibility(stateJisoo);
        nomeJisoo.setVisibility(stateJisoo);
        btnBranco.setVisibility(stateBranco);
        progressoVotacao.setVisibility(stateBarra);
        textoProgresso.setVisibility(stateBarra);

        progressoVotacao.setMax(quantidadeEleitores);
        updateProgress();
    }

    private void montaTelaConfirma() {
        int stateLisa = View.INVISIBLE;
        int stateJisoo = View.INVISIBLE;
        int stateBranco = View.INVISIBLE;
        int stateBarra = View.INVISIBLE;

        switch (escolha) {
            case LISA:
                stateLisa = View.VISIBLE;
                break;
            case JISOO:
                stateJisoo = View.VISIBLE;
                break;
            case BRANCO:
                textoBranco.setVisibility(View.VISIBLE);
                break;
        }

        toggleConfirmation(View.VISIBLE);
        montaTelaPrincipal(stateLisa, stateJisoo, stateBranco, stateBarra);

    }

    public void escolher(View v) {
        int id = v.getId();

        if (id == fotoLisa.getId()) {
            escolha = ESCOLHA.LISA;
        } else if (id == fotoJisoo.getId()) {
            escolha = ESCOLHA.JISOO;
        } else {
            escolha = ESCOLHA.BRANCO;
        }
        montaTelaConfirma();
    }

    public void votar(View v) {
        int id = v.getId();
        if (id == btnConfirma.getId()) {
            switch (escolha) {
                case LISA:
                    votosLisa++;
                    break;
                case JISOO:
                    votosJisoo++;
                    break;
                case BRANCO:
                    votosBrancos++;
                    break;
            }
            eleitorAtual++;
        }

        toggleConfirmation(View.INVISIBLE);
        if (eleitorAtual <= quantidadeEleitores) {
            montaTelaPrincipal();
        } else {
            montaTelaResultado();
        }

    }

    private void toggleConfirmation(int visibility) {
        btnCorrige.setVisibility(visibility);
        btnConfirma.setVisibility(visibility);
    }

    private void updateProgress() {
        textoProgresso.setText(getString(R.string.progresso, eleitorAtual - 1, quantidadeEleitores));
        progressoVotacao.setProgress(eleitorAtual - 1);
    }

    private void montaTelaResultado() {
        montaTelaPrincipal(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);

        int votosValidos = votosLisa + votosJisoo;
        double porcentagemLisa = votosValidos > 0 ? (double) (votosLisa) / (double) (votosValidos) * 100 : 0;
        double porcentagemJisoo = votosValidos > 0 ? (double) (votosJisoo) / (double) (votosValidos) * 100 : 0;

        barraVotosLisa.setMax(votosValidos);
        barraVotosJisoo.setMax(votosValidos);

        barraVotosLisa.setProgress(votosLisa);
        barraVotosJisoo.setProgress(votosJisoo);

        textoResultadoLisa.setText(getString(R.string.resultado_individual, getString(R.string.lisa), votosLisa, String.format("%.2f", porcentagemLisa)));
        textoResultadoJisoo.setText(getString(R.string.resultado_individual, getString(R.string.jisoo), votosJisoo, String.format("%.2f", porcentagemJisoo)));

        String resultado;
        if (votosLisa > votosJisoo) {
            resultado = getString(R.string.novo_sindico, getString(R.string.lisa));
        } else if (votosJisoo > votosLisa) {
            resultado = getString(R.string.novo_sindico, getString(R.string.jisoo));
        } else {
            resultado = getString(R.string.empate);
        }

        resultadoFinal.setText(resultado);

        montaTelaPrincipal(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
        barraVotosLisa.setVisibility(View.VISIBLE);
        barraVotosJisoo.setVisibility(View.VISIBLE);
        textoResultadoLisa.setVisibility(View.VISIBLE);
        textoResultadoJisoo.setVisibility(View.VISIBLE);
        resultadoFinal.setVisibility(View.VISIBLE);

        fotoLisa.setOnClickListener(null);
        fotoJisoo.setOnClickListener(null);
    }

}
