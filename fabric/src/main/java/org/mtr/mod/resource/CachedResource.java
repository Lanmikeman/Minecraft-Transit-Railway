package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class CachedResource<T> {

	@Nullable
	private T data;
	private long expiry;

	private final Supplier<T> dataSupplier;
	private final long lifespan;

	/**
	 * Budget for cold loads only. Hot (already cached) resources never consume this
	 * and always refresh their expiry so in-use models are not GC'd mid-ride.
	 */
	private static int fetchesRemaining;
	private static final int FETCHES_PER_TICK = 16;
	private static final ObjectArrayList<CachedResource<?>> CACHED_RESOURCES = new ObjectArrayList<>();

	public CachedResource(final Supplier<T> dataSupplier, final long lifespan) {
		this.dataSupplier = dataSupplier;
		this.lifespan = lifespan;
		CACHED_RESOURCES.add(this);
	}

	@Nullable
	public T getData(boolean force) {
		final long currentMillis = System.currentTimeMillis();
		if (data != null) {
			// Keep alive while actively requested (each render frame).
			expiry = currentMillis + lifespan;
			return data;
		}
		if (force || fetchesRemaining > 0) {
			data = dataSupplier.get();
			if (!force) {
				fetchesRemaining--;
			}
			expiry = currentMillis + lifespan;
		}
		return data;
	}

	public static void tick() {
		fetchesRemaining = FETCHES_PER_TICK;
		final long currentMillis = System.currentTimeMillis();
		CACHED_RESOURCES.forEach(cachedResource -> {
			if (cachedResource.data != null && currentMillis > cachedResource.expiry) {
				cachedResource.data = null;
			}
		});
	}
}
