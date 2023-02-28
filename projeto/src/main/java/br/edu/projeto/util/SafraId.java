package br.edu.projeto.util;

//esta classe serve para saber de qual safra está sendo cadastradum área, finança ou anotação
public class SafraId
{
	static Integer safra;

	public static Integer getSafra() {
		return safra;
	}

	public static void setSafra(Integer safra) {
		SafraId.safra = safra;
	}
}
