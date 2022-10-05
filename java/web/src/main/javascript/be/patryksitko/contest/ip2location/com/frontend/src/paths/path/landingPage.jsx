import React from "react";
import { connect } from "react-redux";
import {
  ClientIp,
  storeClientIpDataAction,
} from "../../components/client-ip-data";
import useCheckAuthenticationToken from "../../hooks/useCheckAuthenticationToken";
import useFetchClientIpData from "../../hooks/useFetchClientIpData";

const mapStateToProps = ({ state }) => ({
  myIpData: state["my-ip-data"],
  csrfToken: state.cookie.csrfToken,
  fingerprint: state.fingerprint,
  authenticationToken: state.cookie["authentication-token"],
});

const mapDispatchToProps = (dispatch) => ({
  storeClientIpData: (myIpData) => dispatch(storeClientIpDataAction(myIpData)),
});

function LandingPage({
  myIpData,
  csrfToken,
  fingerprint,
  authenticationToken,
  storeClientIpData,
}) {
  useCheckAuthenticationToken(csrfToken, fingerprint, authenticationToken);
  useFetchClientIpData(myIpData, storeClientIpData);
  return (
    <div>
      <ClientIp />
    </div>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(LandingPage);
