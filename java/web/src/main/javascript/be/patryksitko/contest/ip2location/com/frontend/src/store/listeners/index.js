import bindCookiesListener from "./listener/cookies";
import bindFingerprintListener from "./listener/fingerprint";
import bindHistoryListener from "./listener/history";
import bindUpdateTickListener from "./listener/updateTick";
import bindWindowListeners from "./listener/window";

const loop = setInterval;

const storeListeners = (store) => {
  bindHistoryListener(store);
  bindWindowListeners(store);
  bindFingerprintListener(store);
  loop(() => {
    bindUpdateTickListener(store);
    bindCookiesListener(store);
  }, 100);
};

export default storeListeners;
