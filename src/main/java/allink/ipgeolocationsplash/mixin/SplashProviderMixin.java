package allink.ipgeolocationsplash.mixin;

import allink.ipgeolocationsplash.geojs.GeoJSResponse;
import com.google.gson.Gson;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashProviderMixin {
    private static final Gson GSON = new Gson();

    @Inject(at = @At("RETURN"), method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;", cancellable = true)
    public void prepare(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<List<String>> cir) throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://get.geojs.io/v1/ip/geo.json"))
                .build();

        final String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body().trim();
        final GeoJSResponse response = GSON.fromJson(json, GeoJSResponse.class);
        final String splash = "%s: %s, %s (%f %f)".formatted(response.getIp(), response.getCity(), response.getCountry(), response.getLatitude(), response.getLongitude());

        cir.setReturnValue(Collections.singletonList(splash));
    }
}
