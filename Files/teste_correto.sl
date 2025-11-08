// Programa de teste - SimpleLang
// Este programa demonstra todos os recursos da linguagem

int contador;
float media;
string nome;

contador = 0;
media = 7.5;
nome = "Joao Silva";

print("Bem-vindo ao programa!");
print(nome);

print("Digite sua idade:");
int idade;
scan(idade);

if (idade >= 18) {
    print("Voce e maior de idade");
} else {
    print("Voce e menor de idade");
}

print("Contando de 1 a 5:");
contador = 1;
while (contador <= 5) {
    print(contador);
    contador = contador + 1;
}

print("Contando de 5 a 1:");
contador = 5;
do {
    print(contador);
    contador = contador - 1;
} while (contador > 0);

print("Tabuada do 5:");
for (int i = 1; i <= 10; i = i + 1) {
    int resultado;
    resultado = 5 * i;
    print(resultado);
}

int a;
int b;
int c;
a = 10;
b = 5;
c = a + b * 2;
print(c);

float x;
float y;
float z;
x = 10.5;
y = 2.5;
z = x / y + 3.0;
print(z);

if (a > b && c > 15) {
    print("Ambas condicoes sao verdadeiras");
}

if (x >= 10.0 || y <= 3.0) {
    print("Pelo menos uma condicao e verdadeira");
}

int resultado;
resultado = (a + b) * (c - 5);
print(resultado);

if (a == 10) {
    print("a e igual a 10");
}

if (b != 0) {
    print("b e diferente de zero");
}

print("Programa finalizado com sucesso!");