package repository;

import model.Resource;
import exception.InvalidResourceException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private String name;
    private List<Resource> resources = new ArrayList<>();

    public Catalog(String name) {
        this.name = name;
    }

    public void add(Resource res) {
        resources.add(res);
    }

    public void view(Resource res) throws InvalidResourceException {
        Desktop desktop = Desktop.getDesktop();
        try {
            if (res.getLocation().startsWith("http")) {
                // Web adresi ise tarayıcıda aç
                desktop.browse(new URI(res.getLocation()));
            } else {
                // Yerel dosya ise ilgili uygulama ile aç
                File file = new File(res.getLocation());
                if (!file.exists()) throw new IOException("Dosya bulunamadı: " + res.getLocation());
                desktop.open(file);
            }
        } catch (IOException | URISyntaxException e) {
            throw new InvalidResourceException(e);
        }
    }

    @Override
    public String toString() {
        return "Catalog{" + "name='" + name + '\'' + ", resources=" + resources + '}';
    }
}