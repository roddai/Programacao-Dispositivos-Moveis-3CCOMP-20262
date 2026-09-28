# Aula 08 - Formulário de escolha de roupas

Exercício da página 15 do PDF da Aula 08 de Programação para Dispositivos Móveis.
Adaptado dos arquivos Java e XML disponibilizados pelo professor no ZIP da aula.

## Fluxo

`MainActivity → FormActivity → ResultadoActivity`

- Início: título e botão Próximo.
- Formulário: nome da peça, quantidade, bolsos e estampa (CheckBox), peça nova (RadioGroup), cor (Spinner), referência de roupa (Spinner com três fotos), foto (ImageView/seletor) e avaliação (RatingBar).
- Resultado: resumo enviado com `Intent.putExtra("resultado", resultado)` e recebido com `getStringExtra("resultado")`.
- Voltar fecha apenas o resultado e permite editar o formulário.

## Relação com o exemplo do professor

Foram mantidos os nomes das três Activities, os três layouts, ConstraintLayout, ScrollView,
ArrayAdapter, seletor de imagens e a passagem do resumo por Intent.
O campo idade foi adaptado para quantidade e os textos para peças de roupa.
A declaração incompleta do Spinner foi corrigida. O strings.xml, ausente no ZIP,
foi criado com os textos e a lista de cores. AjusteTela mantém o conteúdo fora das
barras do sistema e do teclado.

O exercício usa apenas dados preenchidos na tela: não precisa de API, conta ou banco.
A foto é uma prévia no formulário, como no exemplo; o resultado mostra o resumo textual.

## Abrir

Abra esta pasta Aula08-Roupas no Android Studio, use o JDK incorporado e sincronize o Gradle.
O projeto utiliza SDK 36, Gradle 9.1.0, Android Gradle Plugin 9.0.1 e Java 11 no código.
local.properties aponta para o SDK da máquina e é ignorado pelo Git.
Execute em Android 7.0 (API 24) ou superior.

## Roteiro de teste no emulador

1. Abrir o app e tocar em Próximo.
2. Enviar vazio: o nome deve ser solicitado.
3. Preencher nome e informar quantidade 0: deve aparecer validação.
4. Preencher Camiseta, quantidade 2, marcar bolsos e estampa, selecionar Não, Azul e 4 estrelas.
5. Selecionar Camiseta branca, Camiseta azul e Calça jeans e conferir a troca da foto. Carregar imagem e conferir a prévia. Cancelar o seletor deve manter a foto anterior.
6. Tocar em Ver resultado e conferir todos os valores.
7. Voltar, desmarcar características e zerar avaliação: conferir as mensagens correspondentes.
8. Girar a tela e verificar os campos; testar rolagem com o teclado aberto.

O commit desta aula pode incluir apenas Aula08-Roupas/, separado das renomeações anteriores.

## Validação realizada

Os XMLs, as referências de recursos e as declarações das Activities foram conferidos.
A compilação foi tentada, mas o ambiente de verificação não conseguiu ler o SDK 36
instalado (acesso negado a platforms/android-36/package.xml). Portanto, ainda é
necessário compilar e executar o roteiro acima no Android Studio; nenhum APK foi gerado.
