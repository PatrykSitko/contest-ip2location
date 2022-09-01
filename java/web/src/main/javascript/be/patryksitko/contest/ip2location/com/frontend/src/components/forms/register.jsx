import { useFormik } from "formik";
import httpStatus from "http-status";
import React from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { goBack } from "redux-first-routing";
import * as Yup from "yup";
import "./register.scss";

const validationSchema = Yup.object({
  firstname: Yup.string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Firstname is required."),
  lastname: Yup.string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Lastname is required."),
  email: Yup.string().email().required("Email is required."),
  password: Yup.string().strongPassword().required("Password is required."),
  repassword: Yup.string()
    .required("Retyping password is required.")
    .oneOf([Yup.ref("password"), null], "Passwords don't match!"),
});
const initialValues = {
  firstname: "",
  lastname: "",
  email: "",
  password: "",
  repassword: "",
};
const mapStateToProps = ({
  state: {
    cookie: { "CSRF-TOKEN": csrfToken },
  },
}) => ({ csrfToken });

const mapDispatchToProps = (dispatch) => ({
  goBack: (path) => dispatch(goBack(path)),
});

function LoginForm({ goBack, csrfToken }) {
  const formik = useFormik({
    validationSchema,
    initialValues,
    onSubmit: async (
      { firstname, lastname, email, password },
      { setSubmitting, setErrors }
    ) => {
      const result = await fetch("/api/user/register", {
        headers: {
          "CSRF-TOKEN": csrfToken,
          "Content-Type": "Application/json",
        },
        credentials: "include",
        method: "POST",
        body: JSON.stringify({
          firstname,
          lastname,
          credential: { email, password },
        }),
      });
      const { status, responseType, errors } = await result.json();
      if (httpStatus[status] === httpStatus.CONFLICT) {
        if (responseType === "ERROR") {
          errors.forEach((error) => {
            const startOfErrorMessage = error.indexOf(":") + 1;
            const errorMessage = error.substring(
              startOfErrorMessage,
              error.length
            );
            switch (error.substring(0, startOfErrorMessage)) {
              default:
                break;
              case "[EMAIL]:":
                setErrors({
                  email: errorMessage,
                });
                break;
            }
          });
        }
      }
      setSubmitting(false);
    },
  });
  return (
    <div className="register-form-wrapper">
      <Form className="register-form" onSubmit={formik.handleSubmit}>
        <Form.Group
          className="mb-3 given-names-wrapper firstname"
          controlId="formFirstname"
        >
          <Form.Label className="given-names">First name</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="firstname"
            className="given-names"
            type="text"
            placeholder="Enter first name"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.firstname}
            isValid={!formik.errors.firstname && formik.values.firstname !== ""}
            isInvalid={formik.touched.firstname && formik.errors.firstname}
            feedback={formik.errors.firstname}
          />
          <Form.Control.Feedback type="invalid">
            {formik.errors.firstname}
          </Form.Control.Feedback>
        </Form.Group>
        <Form.Group
          className="mb-3 given-names-wrapper lastname"
          controlId="formLastname"
        >
          <Form.Label className="given-names">Last name</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="lastname"
            className="given-names"
            type="text"
            placeholder="Enter last name"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.lastname}
            isValid={!formik.errors.lastname && formik.values.lastname !== ""}
            isInvalid={formik.touched.lastname && formik.errors.lastname}
            feedback={formik.errors.lastname}
          />
          <Form.Control.Feedback type="invalid">
            {formik.errors.lastname}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3" controlId="formEmail">
          <Form.Label className="email">Email address</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="email"
            type="email"
            placeholder="Enter email"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.email}
            isValid={!formik.errors.email && formik.values.email !== ""}
            isInvalid={formik.touched.email && formik.errors.email}
            feedback={formik.errors.email}
          />
          <Form.Control.Feedback type="invalid">
            {formik.errors.email}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group
          className="mb-3 form-group-password"
          controlId="formPassword"
        >
          <Button className="go-back" onClick={goBack}>
            go back
          </Button>
          <Form.Label className="password-label">Password</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="password"
            type="password"
            placeholder="Password"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.password}
            isValid={!formik.errors.password && formik.values.password !== ""}
            isInvalid={formik.touched.password && formik.errors.password}
            feedback={formik.errors.password}
          />
        </Form.Group>
        <div visible={formik.errors.password} className="feedback-invalid">
          {formik.errors.password}
        </div>

        <Form.Group className="mb-3" controlId="formRetypePassword">
          <Form.Label className="password-label">Retype Password</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="repassword"
            type="password"
            placeholder="Retype Password"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.repassword}
            isValid={
              !formik.errors.repassword && formik.values.repassword !== ""
            }
            isInvalid={formik.touched.repassword && formik.errors.repassword}
            feedback={formik.errors.repassword}
          />
        </Form.Group>
        <div className="feedback-invalid">{formik.errors.repassword}</div>
        <Button
          variant="primary"
          type="submit"
          className="register"
          disabled={!(formik.isValid && formik.dirty) || formik.isSubmitting}
        >
          Register
        </Button>
      </Form>
    </div>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);
