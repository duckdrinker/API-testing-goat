class ArticlesController < ApplicationController
  def index
    render json: Article.all
  end

  def create
    article = Article.create!(params.permit(:title))
    render json: article
  end

  def show
    render json: Article.find(params[:id])
  end
end
