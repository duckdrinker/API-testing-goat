<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="LoginPage.aspx.cs" Inherits="OWASP.WebGoat.NET.LoginPage" %>
<html><body><form runat="server">
  <asp:TextBox ID="User" runat="server" /><asp:TextBox ID="Pass" runat="server" TextMode="Password" />
  <asp:Button ID="ButtonLogOn" runat="server" Text="Log on" OnClick="ButtonLogOn_Click" />
</form></body></html>
