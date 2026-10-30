module Api
  module V1
    class FiltersController < ApplicationController
      def index
        render json: Filter.all
      end

      def update
        filter = Filter.find(params[:id])
        filter.update!(amount: params[:amount])
        render json: filter
      end
    end
  end
end
