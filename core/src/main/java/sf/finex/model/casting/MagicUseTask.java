/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sf.finex.model.casting;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MagicUseTask implements Runnable {

	public final Cast cast;
	public int phase = 1;

	public MagicUseTask(Cast cast) {
		this.cast = cast;
	}

	@Override
	public void run() {
		try {
			switch (phase) {
				case 1:
					cast.launch(this);
					break;
				case 2:
					cast.hit(this);
					break;
				case 3:
					cast.finish(this);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			if (cast.isSimultaneously()) {
				cast.getCaster().setIsCastingSimultaneouslyNow(false);
			} else {
				cast.getCaster().setIsCastingNow(false);
			}
			log.error("Failed on executing MagicUseTaske on {} phase on {} actor.", phase, cast.getCaster(), e);
		}
	}

}
