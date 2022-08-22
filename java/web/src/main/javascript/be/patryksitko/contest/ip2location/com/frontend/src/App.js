import React from "react";
import { Provider } from "react-redux";
import { BrowserRouter as Router } from "react-router-dom";
import "./App.scss";
import Menu from "./components/menu";
import Paths from "./paths";
import store from "./store";
import { browserHistory } from "./store/configuration";

function App() {
  return (
    <Provider store={store}>
      <Menu />
      <Router history={browserHistory}>
        <Paths />
      </Router>
    </Provider>
  );
}

export default App;
