package br.edu.projeto.util;

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
