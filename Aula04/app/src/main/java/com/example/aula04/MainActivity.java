public class MainActivity extends AppCompatActivity {

    Livro livro1, livro2, livro3;
    TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciando os livros
        livro1 = new Livro(
                "Desenvolvendo seu Primeiro Aplicativo Android",
                "Luiz Carlos",
                2020
        );

        livro2 = new Livro(
                "Programação de Jogos Android",
                "Edgard B. Damiani",
                2018
        );

        livro3 = new Livro(
                "Desenvolvimento de Aplicativos: Um Guia Prático",
                "Autor Exemplo",
                2022
        );

        textInfo = findViewById(R.id.textInfo);

        Button button1 = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(v ->
                textInfo.setText(livro1.getInfo())
        );

        button2.setOnClickListener(v ->
                textInfo.setText(livro2.getInfo())
        );

        button3.setOnClickListener(v ->
                textInfo.setText(livro3.getInfo())
        );
    }
}