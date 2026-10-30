const { DataTypes } = require('sequelize')
const sequelize = require('./db')


exports.Page = sequelize.define('Page', { title: DataTypes.STRING })
