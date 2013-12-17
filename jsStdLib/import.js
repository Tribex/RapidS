function import(path) {
	if (path.class == 'JAVAClass') {
		importClass(path);
	} else {
		importPackage(path);
	}
}
import(Packages.org.eclipse.swt);