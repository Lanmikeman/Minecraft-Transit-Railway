package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MovingSoundInstanceExtension;

public class VehicleLoopingSoundInstance extends MovingSoundInstanceExtension {

	public VehicleLoopingSoundInstance(SoundEvent event) {
		super(event, SoundCategory.getBlocksMapped());
		setIsRepeatableMapped(true);
		setRepeatDelay(0);
		setVolume(0);
		setPitch(1);
	}

	public void setData(float volume, float pitch, BlockPos blockPos) {
		setPitch(pitch == 0 ? 1 : pitch);
		// Keep a tiny audible floor while "playing" to avoid stop/restart thrash near 0.
		final float clampedVolume = volume <= 0 ? 0 : Math.max(volume, 0.0001F);
		setVolume(clampedVolume);
		setX(blockPos.getX());
		setY(blockPos.getY());
		setZ(blockPos.getZ());

		final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		final boolean playing = soundManager.isPlaying(new SoundInstance(this));
		if (playing) {
			if (volume <= 0) {
				soundManager.stop(new SoundInstance(this));
			}
		} else if (volume > 0) {
			setIsRepeatableMapped(true);
			setRepeatDelay(0);
			soundManager.play(new SoundInstance(this));
		}
	}

	@Override
	public void tick2() {
	}

	public boolean shouldAlwaysPlay2() {
		return true;
	}

	public boolean canPlay2() {
		return true;
	}

	public void dispose() {
		setDone2();
		final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		soundManager.stop(new SoundInstance(this));
	}
}
