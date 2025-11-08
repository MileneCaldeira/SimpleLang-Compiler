import java.util.Scanner;

public class GeneratedCode {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nomeAluno;
        float nota1;
        float nota2;
        float nota3;
        float nota4;
        float media;
        float mediaAprovacao;
        System.out.println("========================================");
        System.out.println("   SISTEMA DE CALCULO DE MEDIA ESCOLAR");
        System.out.println("========================================");
        System.out.println("");
        System.out.println("Digite o nome do aluno:");
        nomeAluno = scanner.nextLine();
        System.out.println("");
        System.out.println("Digite a nota do 1o bimestre:");
        nota1 = scanner.nextFloat();
        System.out.println("Digite a nota do 2o bimestre:");
        nota2 = scanner.nextFloat();
        System.out.println("Digite a nota do 3o bimestre:");
        nota3 = scanner.nextFloat();
        System.out.println("Digite a nota do 4o bimestre:");
        nota4 = scanner.nextFloat();
        System.out.println("");
        System.out.println("Digite a media minima para aprovacao:");
        mediaAprovacao = scanner.nextFloat();
        media = (nota1 + nota2 + nota3 + nota4) / 4.0f;
        System.out.println("");
        System.out.println("========================================");
        System.out.println("           BOLETIM ESCOLAR");
        System.out.println("========================================");
        System.out.println("");
        System.out.println("Aluno:");
        System.out.println(nomeAluno);
        System.out.println("");
        System.out.println("Notas:");
        System.out.println(nota1);
        System.out.println(nota2);
        System.out.println(nota3);
        System.out.println(nota4);
        System.out.println("");
        System.out.println("Media Final:");
        System.out.println(media);
        System.out.println("");
        System.out.println("========================================");
        if (media >= mediaAprovacao) {
        System.out.println("         SITUACAO: APROVADO!");
        System.out.println("========================================");
        System.out.println("");
        System.out.println("Parabens! Continue assim!");
        }
 else {
        float falta;
        falta = mediaAprovacao - media;
        System.out.println("         SITUACAO: REPROVADO");
        System.out.println("========================================");
        System.out.println("");
        System.out.println("Faltaram pontos:");
        System.out.println(falta);
        System.out.println("");
        System.out.println("Estude mais no proximo periodo!");
        }
        System.out.println("");
        System.out.println("========================================");
        System.out.println("            FIM DO BOLETIM");
        System.out.println("========================================");
        scanner.close();
    }
}
