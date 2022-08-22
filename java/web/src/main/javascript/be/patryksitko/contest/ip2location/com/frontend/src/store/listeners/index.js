import bindHistoryListener from "./listener/history";
import bindWindowListeners from "./listener/window";
import bindUpdateTickListener from "./listener/updateTick";
import bindAuthenticationFingerprintListener from "./listener/authentication/fingerprint";
import bindAuthenticationTokenListener from "./listener/authentication/token";

const loop = setInterval;

export default (store) => {
  bindHistoryListener(store);
  bindWindowListeners(store);
  bindAuthenticationFingerprintListener(store);
  loop(() => {
    bindUpdateTickListener(store);
    bindAuthenticationTokenListener(store);
  }, 100);
};
