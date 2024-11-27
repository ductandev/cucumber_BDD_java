Feature: User Login

  Background: The user starts on the login page
    Given the user is on the login page

  # Trường hợp: Đăng nhập thành công
  Scenario: Login successfully with valid credentials
    When the user enters valid credentials
    And clicks the "로그인" button
    Then the user should be redirected to the home page
    # And see a welcome message

  # Trường hợp: Sai mật khẩu
  Scenario: Login fails with incorrect password
    When the user enters a valid username
    And enters an incorrect password
    And clicks the "로그인" button
    Then the user should see an error message "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요"
    And the user remains on the login page

  # Trường hợp: Tài khoản không tồn tại
  Scenario: Login fails for non-existent account
    When the user enters a non-existent username
    And any password
    And clicks the "로그인" button
    Then the user should see an error message "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요"
    And the user remains on the login page

  # Trường hợp: Trường thông tin bị bỏ trống
  Scenario: Login fails when username or password is empty
    When the user leaves the username or password field empty
    And clicks the "로그인" button
    Then the user should see an error email message "이메일 아이디를 입력해주세요."
    And the user should see an error password message "비밀번호를 입력해주세요."
