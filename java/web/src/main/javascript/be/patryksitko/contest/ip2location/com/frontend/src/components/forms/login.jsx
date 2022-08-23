import React from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { push } from "redux-first-routing";
import "./login.scss";

const mapDispatchToProps = (dispatch) => ({
  changePath: (path) => dispatch(push(path)),
});

function LoginForm({ changePath }) {
  return (
    <Form className="login-form">
      <Form.Group className="mb-3" controlId="formBasicEmail">
        <Form.Label>Email address</Form.Label>
        <Form.Control type="email" placeholder="Enter email" />
        <Form.Text className="text-muted">
          I'll never share your email with anyone else.
        </Form.Text>
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicPassword">
        <Form.Label>Password</Form.Label>
        <Form.Control type="password" placeholder="Password" />
      </Form.Group>
      <Button variant="primary" type="submit" className="login">
        Login
      </Button>
      <Link to="/forgot-password" className="forgot-password">
        Forgot password?
      </Link>
      <Button
        variant="secondary"
        type="submit"
        className="register"
        onClick={(action) => {
          action.preventDefault();
          changePath("/register");
        }}
      >
        Register
      </Button>
    </Form>
  );
}

export default connect(null, mapDispatchToProps)(LoginForm);
