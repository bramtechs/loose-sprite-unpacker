package be.brambasiel.looseunpacker.test;

import java.io.IOException;

import be.brambasiel.looseunpacker.LooseUnpackerLauncher;

public class LooseUnpackerTest {

	public static void main(String[] args) throws IOException {
		LooseUnpackerLauncher.main(new String[]{"./example/shapes.png","example/out"});
	}

}
