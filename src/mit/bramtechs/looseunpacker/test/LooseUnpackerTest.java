package mit.bramtechs.looseunpacker.test;

import java.io.IOException;

import mit.bramtechs.looseunpacker.LooseUnpackerLauncher;

public class LooseUnpackerTest {

	public static void main(String[] args) throws IOException {
		LooseUnpackerLauncher.main(new String[]{"./example/shapes.png","example/out"});
	}

}
